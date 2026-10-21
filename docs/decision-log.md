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

## 3.4 Feature Package Structure

**Decision**

Domain entities live in their feature packages (`user`, `company`, `joboffer`, `jobapplication`, `interview`, `task`). `BaseEntity` lives in `shared.persistence`.

**Context**

Entities were initially placed in a layer-oriented `domain.model` package, contradicting ADR-0002.

**Rationale**

Feature packages keep each capability cohesive and allow package-private visibility to enforce boundaries between features. The resulting dependency graph is acyclic: `user ← company ← joboffer ← jobapplication ← interview / task`.

**Consequences**

Cross-feature access goes through public services rather than repositories.

---

## 3.5 Owner-Scoped Selective Repositories

**Decision**

Repositories extend `Repository<T, UUID>` and declare only the methods they need. Repositories of user-owned entities expose only owner-scoped queries. Repositories are package-private within their feature.

**Context**

`JpaRepository` exposes unscoped operations such as `findAll()` and `findById(id)`.

**Rationale**

Unscoped access methods make insecure direct object references possible through a single mistake. Removing them enforces user isolation (BI-001) by construction.

**Consequences**

Methods must be declared explicitly. Access to another user's resource is indistinguishable from access to a non-existent one and results in a 404 response.

---

## 3.6 Stateless Security Foundation

**Decision**

Spring Security is configured as stateless from the first endpoint: no HTTP sessions, CSRF protection, form login or HTTP Basic. `/api/v1/auth/**` and `/error` are public; every other request requires authentication, and unauthenticated requests receive `401 Unauthorized`.

**Context**

CareerHub exposes a REST API consumed by a separate frontend and will use token-based authentication.

**Rationale**

Denying access by default prevents new endpoints from being exposed accidentally. CSRF protection targets cookie-based sessions, which the API does not use. `/error` must remain public so that errors raised by public endpoints are not masked as `401` responses.

**Consequences**

Every new endpoint is protected unless it is explicitly opened. Token-based authentication will be added on top of this configuration.

---

## 3.7 Delegating Password Encoder

**Decision**

Passwords are hashed with Spring Security's `DelegatingPasswordEncoder`, which currently uses BCrypt.

**Context**

Passwords must be stored as secure one-way hashes, and the preferred hashing algorithm may change over the lifetime of the project.

**Rationale**

Stored hashes carry an algorithm prefix (for example `{bcrypt}`), so the algorithm can be replaced later without invalidating existing hashes. BCrypt is deliberately slow and widely supported.

**Consequences**

`password_hash` values include the algorithm prefix. BCrypt only processes the first 72 bytes of a password, which constrains the password policy (see 6.8).

---

## 3.8 Command Objects as Service Input

**Decision**

Services receive command objects defined in their own feature (for example `RegisterUserCommand`) instead of API request DTOs.

**Context**

Request DTOs belong to the HTTP contract and carry web validation annotations.

**Rationale**

Keeping services independent of the API allows them to be reused from other entry points and tested without the web layer. A dedicated type also avoids long lists of positional `String` parameters, where swapped arguments compile silently.

**Consequences**

Controllers map request DTOs to commands. Services may return entities to the web layer, which maps them to response DTOs; ADR-0006 applies at the API boundary. Records that contain secrets override `toString()` so that the secret never reaches logs.

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

Job applications have no natural key. They are identified exclusively by their UUID, and no uniqueness constraint is applied to company and position title.

**Context**

Two applications with the same company and position title may represent different opportunities, for example the same role in different locations.

**Rationale**

Location context is available through the associated JobOffer when one exists. Enforcing a composite natural key would reject valid scenarios.

**Consequences**

Duplicate detection, if ever needed, will be a user-facing warning rather than a database constraint.

*Revised on 2026-10-18: the original wording required location context that the JobApplication model does not store.*

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

## 4.8 Application Status Set

**Decision**

`JobApplication.status` uses: `SAVED`, `APPLIED`, `INTERVIEWING`, `OFFER`, `ACCEPTED`, `REJECTED`, `WITHDRAWN`.

**Context**

The status values were undefined, and the optionality of `appliedAt` depends on them.

**Rationale**

`SAVED` supports tracking opportunities before applying. `WITHDRAWN` and `REJECTED` distinguish who closed the process, which is valuable for future analytics.

**Consequences**

Transition rules are deferred to the service layer phase.

---

## 4.9 Optional appliedAt for Saved Applications

**Decision**

`appliedAt` is a `LocalDate`, required for every status except `SAVED`.

**Context**

An application in `SAVED` status has not been submitted yet.

**Rationale**

Requiring a submission date for unsubmitted applications would force users to enter false data.

**Consequences**

The rule is enforced in the service layer; the database column is nullable.

---

## 4.10 Default Application Priority

**Decision**

`JobApplication.priority` is mandatory in persistence and defaults to `MEDIUM` when the client does not provide it.

**Context**

The data model described priority as optional, while the schema required it.

**Rationale**

A default avoids forcing an irrelevant choice on users while keeping filtering and sorting free of null handling. It is consistent with `Task.priority`.

**Consequences**

The API treats priority as optional; the service layer assigns the default.

---

## 4.11 Case-Insensitive Email Identity

**Decision**

Email addresses are trimmed and converted to lowercase with `Locale.ROOT` before they are stored or looked up (`User.normalizeEmail`).

**Context**

Users often type the same address with different capitalization, while the database unique constraint is case-sensitive.

**Rationale**

Although RFC 5321 allows a case-sensitive local part, mainstream email providers treat addresses case-insensitively. Normalizing prevents duplicate accounts and failed lookups. `Locale.ROOT` avoids locale-dependent case mappings such as the Turkish dotless i.

**Consequences**

Email uniqueness is effectively case-insensitive. A dedicated `EmailAddress` value object is deferred until more behavior justifies it.

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

## 5.5 DATE for Calendar Dates

**Decision**

Calendar dates (`appliedAt`, `publishedAt`, `expiresAt`) are stored as `DATE` and mapped to `LocalDate`.

**Context**

These fields were stored as `TIMESTAMP WITH TIME ZONE`.

**Rationale**

They represent calendar days, not instants. Timestamps introduce time-zone conversions that can shift the stored day.

**Consequences**

Instants such as `scheduledAt`, `dueAt` and audit fields remain timestamps with time zone.

---

## 5.6 Explicit Foreign Key Indexes

**Decision**

Every foreign key column is explicitly indexed.

**Context**

PostgreSQL does not automatically index foreign key columns.

**Rationale**

Foreign keys support ownership queries and the restrictive deletion policy, which checks child tables when a parent is deleted.

**Consequences**

Indexes are introduced through a Flyway migration.

---

## 5.7 Instant for Points in Time

**Decision**

Audit timestamps and event instants (`createdAt`, `updatedAt`, `scheduledAt`, `dueAt`) are mapped to `Instant`.

**Context**

These fields were mapped to `OffsetDateTime`, while the data model specified `Instant`.

**Rationale**

`TIMESTAMP WITH TIME ZONE` stores an absolute instant and does not preserve the original offset. `Instant` represents exactly what is stored, and its equality does not depend on the JVM time zone. Values are truncated to microseconds to match PostgreSQL precision.

**Consequences**

Time-zone conversion for display is the responsibility of the presentation layer.

---

## 5.8 Lazy Loading and Disabled Open Session in View

**Decision**

All `@ManyToOne` relationships use `FetchType.LAZY`, and `spring.jpa.open-in-view` is disabled.

**Context**

JPA defaults `@ManyToOne` to eager loading, and Spring Boot keeps the persistence session open during web rendering by default.

**Rationale**

Eager loading causes unnecessary queries and N+1 problems. Open session in view hides lazy-loading access outside the service layer.

**Consequences**

Services must explicitly load the data each use case requires. Accessing an uninitialized relationship outside a transaction fails with `LazyInitializationException`.

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

## 6.6 Problem Details for Error Responses

**Decision**

Error responses follow RFC 9457 (Problem Details) and are produced by a global `@RestControllerAdvice` that extends `ResponseEntityExceptionHandler`:

- Domain conflicts extend the shared `ConflictException` and map to `409` with the exception message.
- `DataIntegrityViolationException` maps to `409` with a generic message.
- Request validation failures map to `400` with an `errors` object of field names and messages.
- Any other exception maps to `500` with a generic message.

**Context**

Clients need a consistent, machine-readable error format, and error responses must not expose internal details.

**Rationale**

Problem Details is a standard format supported natively by Spring. A semantic base exception in `shared` keeps dependencies pointing from features to `shared`, never the reverse. Database messages contain constraint names and duplicated values, which may be personal data, so they are never returned. Rejected values are never echoed, so a rejected password cannot appear in a response.

**Consequences**

Features can add new exceptions without modifying the global handler. In the rare race between two concurrent registrations with the same email, the losing request receives the generic `409` message. Violations of the restrictive deletion policy will also map to `409`. Validation messages may be localized according to the request locale, so clients must rely on field names rather than message text. Authentication failures (`401`) currently return an empty body.

---

## 6.7 Duplicate Email Registration Response

**Decision**

Registering an email address that already exists returns `409 Conflict` with an explicit message.

**Context**

A registration endpoint must report why a valid request cannot be completed.

**Rationale**

An explicit message gives the user a clear next step. The resulting account enumeration risk is accepted: registration inherently reveals whether an address is in use, and mitigations such as rate limiting or email verification are deferred.

**Consequences**

The service checks for an existing email before hashing the password, to avoid unnecessary hashing work. The database unique constraint remains the final guarantee; `saveAndFlush` ensures that a violation surfaces inside the service transaction.

---

## 6.8 Password Length Policy

**Decision**

Passwords must contain between 8 and 64 characters and must not exceed 72 bytes when encoded as UTF-8.

**Context**

BCrypt only processes the first 72 bytes of a password, and Spring Security rejects longer input with an exception. `@Size` counts UTF-16 code units, not bytes, so a 64-character limit alone does not guarantee 72 bytes for multibyte characters.

**Rationale**

The character limit is the user-facing rule. The byte limit, implemented by the reusable `@Utf8ByteSize` constraint, rejects multibyte input that would otherwise cause a `500` response. Pre-hashing the password before BCrypt was rejected because it is a non-standard construction.

**Consequences**

Both limits are enforced at the API boundary and return `400` with a field error on `password`.

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

## 8.7 Argon2 Password Hashing

**Status:** Deferred

Argon2 has no 72-byte input limit and is recommended by current password-storage guidance, but it requires an additional cryptography dependency (BouncyCastle).

`DelegatingPasswordEncoder` allows a future migration without invalidating existing BCrypt hashes.

---

# 9. Maintenance

This document should be updated when a significant project decision is made.

Existing decisions should not be silently rewritten when the decision changes.

When a decision is fundamentally reversed or superseded, the original decision should remain documented and its status should be updated or linked to the new decision.

Significant architectural decisions should additionally receive a dedicated Architecture Decision Record under:

    docs/adr/
