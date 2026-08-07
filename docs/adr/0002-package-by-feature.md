# ADR-0002: Organize Code by Feature

**Status:** Accepted

## Context

As CareerHub grows, the codebase will contain multiple business domains, including authentication, companies, job offers, applications, interviews, and tasks.

A traditional package-by-layer organization (controllers, services, repositories, etc.) is simple for small applications but becomes increasingly difficult to navigate as the number of features grows.

The project aims to remain maintainable over several years, making modularity and discoverability important design goals.

## Decision

The backend and frontend are organized primarily by feature rather than by technical layer.

Each feature encapsulates its own components, including controllers, services, repositories, DTOs, validation logic, and other feature-specific classes.

Cross-cutting concerns such as security, configuration, and shared utilities are placed in dedicated shared modules.

This organization aligns both the backend and frontend under the same architectural philosophy.

## Consequences

### Positive

- Improves modularity and maintainability.
- Reduces navigation across unrelated packages.
- Keeps business functionality grouped together.
- Makes the project easier to understand as it grows.
- Encourages feature ownership and encapsulation.
- Provides consistency between backend and frontend.

### Negative

- Requires discipline to avoid placing unrelated code inside feature modules.
- Shared code must be carefully managed to prevent the `shared` package from becoming a miscellaneous container.

These trade-offs are acceptable given the expected long-term evolution of the project.

## Alternatives Considered

### Package by Layer

Example:

```
controller/
service/
repository/
entity/
dto/
```

Rejected because related business logic becomes scattered across multiple packages as the application grows.

### Hybrid Organization

A hybrid structure combining layers and features was considered.

Rejected because it increases ambiguity regarding where new classes should be placed.

A feature-oriented organization provides clearer boundaries and scales better for the expected size of the project.
