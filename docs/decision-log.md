# CareerHub Decision Log

## 1. Purpose

This document records the significant design and development decisions made during the creation of CareerHub.

Its purpose is to preserve the reasoning behind important decisions so that they can be understood and evaluated later, even as the project evolves.

This document complements the Architecture Decision Records (ADRs).

The decision log provides a concise overview of the decisions made throughout the project, while individual ADRs provide a more detailed analysis for decisions that require deeper documentation.

---

## 2. Decision Format

Each decision is documented using the following structure:

- **Decision** — What was decided.
- **Context** — Why the decision was necessary.
- **Rationale** — Why the chosen option was preferred.
- **Consequences** — Important implications of the decision.

Not every implementation detail requires an entry in this document. Only decisions with meaningful architectural, domain, database, API, or development impact should be recorded.

---

# 3. Architectural Decisions

## 3.1 Layered Architecture

**Decision**

Use a layered architecture for the backend.

**Context**

CareerHub requires a clear separation between API handling, business logic, and persistence.

**Rationale**

Layered architecture is well understood, suitable for the project's scope, and provides a strong structure for learning backend development without introducing unnecessary architectural complexity.

**Consequences**

The backend will separate responsibilities into layers such as:

- Controller
- Service
- Repository
- Persistence

Future architectural changes can be introduced if the project grows beyond the capabilities of the initial structure.

---

## 3.2 Feature-Oriented Organization

**Decision**

Organize backend and frontend code primarily by feature while maintaining clear internal structure.

**Context**

Pure technical-layer organization can cause related functionality to become distributed across large packages or directories.

**Rationale**

Feature-oriented organization keeps related functionality together while preserving the architectural separation established by the layered architecture.

**Consequences**

Each feature may contain its relevant controllers, services, DTOs, persistence components, and other implementation details.

The exact package structure may evolve during implementation.

---

## 3.3 DTO-Based API

**Decision**

The API will use dedicated request and response DTOs rather than exposing persistence entities directly.

**Context**

JPA entities represent persistence concerns and should not automatically define the public API contract.

**Rationale**

DTOs provide separation between the API and persistence models and allow either model to evolve independently.

**Consequences**

Additional mapping code is required, but API stability and maintainability are improved.

---

# 4. Domain Decisions

## 4.1 JobApplication as an Independent Entity

**Decision**

`JobApplication` is modeled as an independent domain entity rather than being represented only through a relationship between `Company` and `JobOffer`.

**Context**

A user may apply to a company without having a specific job offer stored in CareerHub.

**Rationale**

The application itself is a first-class piece of career-management information.

**Consequences**

A `JobApplication` requires a `Company` but may optionally reference a `JobOffer`.

---

## 4.2 Optional JobOffer in JobApplication

**Decision**

`JobApplication` may optionally reference a `JobOffer`.

**Context**

Not every application originates from a specific stored job offer.

**Rationale**

Requiring a `JobOffer` would unnecessarily restrict valid application scenarios.

**Consequences**

The domain supports both:

- Applications associated with a specific JobOffer.
- Applications made directly to a Company.

---

## 4.3 JobOffer Must Belong to the Application's Company

**Decision**

When a `JobApplication` references a `JobOffer`, that JobOffer must belong to the same Company referenced by the application.

**Context**

Without this constraint, the domain could represent an application to one company using an offer from another company.

**Rationale**

The relationship must remain semantically consistent.

**Consequences**

The backend must validate this business rule.

The frontend should also prevent users from selecting incompatible combinations.

The domain relationship is effectively:

    JobApplication
    ├── Company (required)
    └── JobOffer (optional)
            └── must belong to Company

---

## 4.4 Application Identity

**Decision**

A job application is distinguished using its associated company, position title, and location context.

**Context**

Two apparently identical offers may represent different opportunities if they correspond to different locations.

**Rationale**

Location can materially distinguish employment opportunities.

**Consequences**

The application model must retain sufficient context to distinguish these cases even when other information is identical.

---

## 4.5 Interview as an Independent Entity

**Decision**

`Interview` is modeled as a separate entity associated with `JobApplication`.

**Context**

A single application may involve multiple interviews.

**Rationale**

An interview has its own lifecycle, schedule, format, and notes.

**Consequences**

The relationship is:

    JobApplication 1 ─── N Interview

---

## 4.6 Task with Optional JobApplication

**Decision**

A `Task` may optionally reference a `JobApplication`.

**Context**

Some tasks are related to a specific application, while others are general career-management tasks.

**Rationale**

Requiring every task to belong to an application would prevent useful tasks such as updating a CV or preparing a general certification.

**Consequences**

The model supports both:

    Task → JobApplication

and:

    Task → null

When a JobApplication is provided, it must belong to the same User as the Task.

---

## 4.7 Minimal Interview Context

**Decision**

Interview participants and locations are represented using simple strings in the MVP.

**Context**

CareerHub does not currently manage external interviewers or meeting providers as independent domain entities.

**Rationale**

Creating dedicated entities for these concepts would introduce unnecessary complexity at the current stage.

**Consequences**

`interviewer` and `location` remain free-form values.

They may be modeled more explicitly in a future version if integrations or advanced scheduling functionality require it.

---

# 5. Database Decisions

## 5.1 PostgreSQL

**Decision**

Use PostgreSQL as the primary relational database.

**Context**

CareerHub contains strongly relational domain data with multiple relationships and integrity constraints.

**Rationale**

PostgreSQL provides a mature relational model, strong constraint support, excellent Java/Spring integration, and relevant real-world experience for the project's learning goals.

**Consequences**

The persistence model will be relational and designed around PostgreSQL capabilities.

---

## 5.2 Flyway for Database Migrations

**Decision**

Use Flyway to manage database schema migrations.

**Context**

The database schema will evolve throughout development.

**Rationale**

Version-controlled migrations provide a reproducible and explicit database evolution process.

**Consequences**

Database changes must be represented through Flyway migration files rather than relying exclusively on automatic Hibernate schema generation.

---

## 5.3 BaseEntity

**Decision**

Create a shared `BaseEntity` containing common persistence metadata.

**Context**

Multiple entities require common fields such as identifiers and timestamps.

**Rationale**

Centralizing common persistence metadata avoids unnecessary duplication.

**Consequences**

Domain entities inherit or otherwise reuse:

- `id`
- `createdAt`
- `updatedAt`

The exact JPA implementation will be defined during backend development.

---

## 5.4 Restrictive Deletion

**Decision**

Prefer restrictive deletion behavior over automatic cascading deletion for dependent domain data.

**Context**

Automatic cascading deletion can unintentionally remove valuable career-management information.

**Rationale**

Explicit deletion provides greater control over data integrity and makes destructive operations more predictable.

**Consequences**

Relationships must be designed so that dependent records prevent deletion when appropriate.

Application code must explicitly handle dependent data rather than relying on broad database cascades.

---

# 6. API Decisions

## 6.1 API Versioning

**Decision**

Version the API through the URL using `/api/v1`.

**Context**

The API may evolve and introduce breaking changes in future versions.

**Rationale**

URL-based versioning is explicit and easy to understand for clients and developers.

**Consequences**

Breaking API changes should result in a new API version.

---

## 6.2 REST Resource Orientation

**Decision**

Design the API around domain resources rather than individual database operations.

**Context**

The API should represent CareerHub concepts rather than expose persistence implementation details.

**Rationale**

Resource-oriented APIs provide a clearer and more stable contract.

**Consequences**

Primary resources include:

- Companies
- Job Offers
- Applications
- Interviews
- Tasks
- Dashboard

---

## 6.3 Ownership Enforcement

**Decision**

All user-owned resources must be scoped to the authenticated User.

**Context**

CareerHub stores private career-management data.

**Rationale**

Data ownership must be enforced by the backend regardless of frontend behavior.

**Consequences**

Every protected resource access must verify that the authenticated user owns or is authorized to access the requested resource.

---

## 6.4 Pagination Deferred

**Decision**

Pagination is not a mandatory MVP requirement but the API should allow it to be introduced later.

**Context**

Initial CareerHub datasets are expected to be relatively small.

**Rationale**

Implementing pagination everywhere from the beginning would add complexity without providing significant MVP value.

**Consequences**

Collection endpoints should be designed so pagination can be introduced without fundamentally changing the resource model.

---

## 6.5 API and Persistence Separation

**Decision**

The public API contract must remain independent from JPA entities.

**Context**

Persistence models may change for database or implementation reasons.

**Rationale**

Coupling the API directly to persistence entities makes future changes more difficult and can unintentionally expose internal implementation details.

**Consequences**

Request and response DTOs are part of the API boundary.

---

# 7. Development Decisions

## 7.1 Incremental Development

**Decision**

Develop CareerHub incrementally through functional vertical slices.

**Context**

The project is intended both as a usable application and as a learning project.

**Rationale**

Vertical development provides earlier feedback and reduces the risk of building large disconnected technical layers.

**Consequences**

Features will generally progress through:

    Domain
      ↓
    Backend
      ↓
    Tests
      ↓
    Frontend
      ↓
    Integration
      ↓
    Verification

---

## 7.2 MVP Scope Control

**Decision**

Keep the MVP intentionally limited.

**Context**

CareerHub has many possible future capabilities, particularly around AI, analytics, documents, and integrations.

**Rationale**

Trying to implement all envisioned functionality initially would increase complexity and reduce the likelihood of completing a coherent product.

**Consequences**

Features such as AI assistance, calendar integrations, notifications, advanced analytics, and document management remain outside the MVP.

---

## 7.3 Learning Through Implementation

**Decision**

Technical areas that are unfamiliar to the developer will be learned and implemented as part of the project rather than excluded solely because they are unfamiliar.

**Context**

CareerHub is intentionally both a portfolio project and a learning project.

**Rationale**

Using realistic technologies and development practices provides greater educational value than restricting the project to already familiar tools.

**Consequences**

Implementation may require additional investigation and documentation, particularly for technologies such as Flyway, Spring Security, Docker, and production-oriented testing.

Complexity should nevertheless remain proportional to the project's goals.

---

# 8. Rejected / Deferred Decisions

## 8.1 Automatic Cascade Deletion

**Status:** Rejected

Automatic cascade deletion was rejected in favor of restrictive deletion behavior.

---

## 8.2 Polymorphic Task Relationships

**Status:** Rejected

A Task will not contain arbitrary relationships such as:

    JobApplication?
    Company?
    JobOffer?
    Interview?

Instead, the MVP uses a simple optional relationship with `JobApplication`.

This avoids an ambiguous and difficult-to-maintain domain model.

---

## 8.3 Dedicated Interviewer Entity

**Status:** Deferred

Interviewers are currently represented as strings.

A dedicated entity may be introduced if future requirements such as contact management or interview integrations justify it.

---

## 8.4 Dedicated Location Entity

**Status:** Deferred

Interview locations are currently represented as strings.

More structured location or meeting information may be introduced if future scheduling or calendar integrations require it.

---

## 8.5 Advanced AI Features

**Status:** Deferred

AI functionality is intentionally excluded from the MVP.

Potential AI capabilities will be evaluated after the core CareerHub workflow has been implemented and validated.

---

## 8.6 Calendar and External Integrations

**Status:** Deferred

Calendar providers, video-conferencing services, notifications, and other external integrations are outside the initial MVP scope.

They may be introduced in future versions if they provide sufficient value.

---

# 9. Maintenance

This document should be updated when a significant project decision is made.

Existing decisions should not be silently rewritten when the decision changes.

When a decision is fundamentally reversed or superseded, the original decision should remain documented and its status should be updated or linked to the new decision.

Significant architectural decisions should additionally receive a dedicated Architecture Decision Record under:

    docs/adr/
