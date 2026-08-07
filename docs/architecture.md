# Architecture

## 1. Architectural Goals

CareerHub is designed with long-term maintainability as its primary architectural objective.

The architecture prioritizes simplicity, modularity, and clear separation of responsibilities over premature optimization.

The system should remain easy to understand, test, extend, and maintain as new features are introduced.

Every architectural decision should support incremental evolution without requiring major redesigns.

### Architectural Principles

- Prefer simplicity over unnecessary complexity.
- Build for maintainability before scalability.
- Separate business logic from infrastructure concerns.
- Keep components loosely coupled and highly cohesive.
- Design the system to evolve incrementally.
- Avoid premature optimization.

## 2. High-Level Architecture

CareerHub follows a client-server architecture.

The frontend is responsible for presenting information and interacting with the user.

All business rules are implemented in the backend, which exposes a RESTful API consumed by the frontend.

The backend is responsible for authentication, validation, business logic, and data persistence.

Persistent data is stored in a PostgreSQL database accessed through Spring Data JPA and Hibernate.

This separation of responsibilities allows each layer to evolve independently while maintaining a clear system architecture.

┌──────────────────────────────┐
│           User               │
└──────────────┬───────────────┘
               │ HTTPS
               ▼
┌──────────────────────────────┐
│      React Frontend          │
│ (Presentation Layer)         │
└──────────────┬───────────────┘
               │ REST API
               ▼
┌──────────────────────────────┐
│     Spring Boot Backend      │
│                              │
│ Controllers                  │
│ Services                     │
│ Repositories                 │
└──────────────┬───────────────┘
               │ JPA/Hibernate
               ▼
┌──────────────────────────────┐
│         PostgreSQL           │
└──────────────────────────────┘

## 3. Architectural Style

CareerHub adopts a traditional Layered Architecture for the backend.

This architectural style separates the application into distinct layers, each with a well-defined responsibility. The goal is to improve maintainability, readability, testability, and long-term evolution of the system.

The backend is organized into the following layers:

- **Controller Layer** – Handles HTTP requests and responses, validates incoming data, and delegates business operations to the service layer.
- **Service Layer** – Contains the application's business logic and coordinates interactions between different domain components.
- **Repository Layer** – Provides data access and persistence through Spring Data JPA.
- **Persistence Layer** – Stores application data in a PostgreSQL database using Hibernate as the ORM framework.

Each layer communicates only with the layer directly below it, reducing coupling and improving separation of concerns.

This architectural style was selected because it offers an excellent balance between simplicity and maintainability for the current scope of CareerHub. While more complex approaches such as Clean Architecture or Hexagonal Architecture were considered, they were intentionally rejected due to the additional complexity they introduce without providing significant benefits for the project's current requirements.

The architecture has been designed to evolve incrementally, allowing future refactoring if the project's complexity justifies a different architectural approach.

## 4. System Components

CareerHub is organized into independent functional components, each responsible for a specific area of the application.

Although all components are deployed as part of the same application, they are designed to remain logically independent to improve maintainability and future scalability.

### Authentication

Responsible for user registration, authentication, authorization, and session management.

---

### Dashboard

Provides an overview of the user's current recruitment activity, including applications, interviews, upcoming tasks, and key metrics.

---

### Companies

Manages organizations that the user is interested in.

This component provides CRUD operations for companies and serves as the organizational context for job offers and applications.

---

### Job Offers

Manages employment opportunities associated with companies.

Job offers may exist independently of applications and provide additional context during the recruitment process.

---

### Applications

Represents the core component of CareerHub.

It manages the lifecycle of every job application, including status changes, notes, and relationships with interviews and tasks.

---

### Interviews

Manages recruitment interviews associated with applications.

Supports multiple interviews for a single application.

---

### Tasks

Manages career-related tasks.

Tasks may either belong to a specific application or exist independently as general career activities.

## 5. Backend Architecture

The backend is implemented using Spring Boot and follows a traditional Layered Architecture.

To improve maintainability as the project grows, the codebase is organized primarily by feature (business capability) rather than by technical layer.

Each feature encapsulates its own controllers, services, repositories, DTOs, mappers, entities, validation logic, and feature-specific exceptions.

This approach improves modularity, reduces navigation across unrelated packages, and allows each feature to evolve independently while maintaining a consistent internal structure.

Cross-cutting concerns such as security, configuration, shared exceptions, and common utilities are placed in dedicated shared packages outside the feature modules.

## 6. Frontend Architecture

The frontend is implemented as a React Single Page Application (SPA) using TypeScript.

Like the backend, the frontend is organized primarily by feature rather than by technical layer.

Each feature encapsulates its own pages, components, hooks, services, validation logic, and type definitions, improving modularity and maintainability.

Cross-cutting concerns such as routing, layouts, theming, and shared UI components are placed in dedicated shared modules.

The frontend communicates exclusively with the backend through the REST API and does not contain business logic beyond presentation concerns.

Application state is divided into three categories:

- Global UI state managed through React Context.
- Server state managed through TanStack Query.
- Local component state managed using React hooks.

This separation keeps the frontend predictable, scalable, and aligned with modern React development practices.

## 7. Data Persistence

CareerHub uses PostgreSQL as its primary relational database.

The backend interacts with the database exclusively through the repository layer using Spring Data JPA.

Hibernate is used as the Object-Relational Mapping (ORM) framework, while Spring Data JPA provides the abstraction layer for data access.

Together, they simplify persistence while keeping the domain model independent from database implementation details.

Business logic must never depend directly on database implementation details. All persistence operations are encapsulated within repositories, ensuring that the service layer remains focused on business rules rather than data access concerns.

Database schema evolution will be managed through version-controlled migrations to ensure reproducibility and consistency across development and deployment environments.

The persistence model is designed to preserve referential integrity and maintain the consistency of the domain model.

## 8. Security Overview

CareerHub follows a stateless authentication model.

All protected resources require successful authentication before access is granted.

Authorization is enforced on a per-user basis, ensuring that users can only access and modify resources they own.

The backend is responsible for authentication, authorization, and request validation.

The frontend never performs authorization decisions and acts solely as a presentation layer.

Sensitive information such as passwords is never stored in plain text and is handled according to current security best practices.

Security mechanisms are designed to be independent of business logic, ensuring that authentication and authorization concerns remain separated from the application's domain model.

## 9. Design Principles

The following principles guide every architectural and implementation decision throughout the project.

### Separation of Concerns

Each component is responsible for a single area of the application. Business logic, persistence, presentation, and infrastructure concerns remain clearly separated.

### Feature-Oriented Organization

The codebase is organized by business capability rather than by technical layer. Each feature encapsulates its own controllers, services, repositories, DTOs, validation logic, and related components.

### Simplicity First

The project prioritizes simple, maintainable solutions over unnecessary complexity. New patterns and technologies are introduced only when they provide a clear benefit.

### Incremental Evolution

The architecture is designed to evolve gradually. New features should extend the existing system without requiring major structural changes.

### Single Source of Truth

Each piece of information should have a single authoritative representation within the system. Data duplication should be avoided whenever possible.

### Clear Boundaries

The frontend is responsible for presentation, while the backend owns business rules and data integrity. Communication between both layers is performed exclusively through the REST API.

### Domain-Driven Design

The domain model drives the system design. Technical decisions should support the business domain rather than dictate it.

### Security by Design

Authentication, authorization, and data ownership are considered fundamental architectural concerns rather than optional additions.

### Testability

The architecture should encourage isolated testing of business logic by keeping components loosely coupled and assigning clear responsibilities.

## 10. Future Evolution

CareerHub has been designed with long-term evolution in mind.

The initial version intentionally focuses on a small but complete feature set, providing a stable foundation before introducing additional complexity.

Future versions may include capabilities such as document management, calendars, notifications, analytics, artificial intelligence, and external platform integrations.

The chosen architecture supports this evolution through independent feature modules, clear separation of responsibilities, and well-defined interfaces between components.

New functionality should be introduced by extending the existing architecture rather than modifying unrelated modules.

Architectural decisions should continue to prioritize maintainability, readability, and simplicity over premature optimization.

As the project grows, additional architectural patterns may be introduced when justified by increasing complexity. However, such changes should always be driven by concrete requirements rather than trends or unnecessary abstraction.
