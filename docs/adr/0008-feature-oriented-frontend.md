# ADR-0008: Organize Frontend by Feature

**Status:** Accepted

## Context

CareerHub's frontend is expected to grow alongside the backend as new features are introduced.

A traditional organization based solely on technical layers (components, pages, hooks, services) becomes increasingly difficult to navigate as the application grows.

The project aims to maintain consistency between frontend and backend architectures.

## Decision

The frontend is organized primarily by feature.

Each feature encapsulates its own pages, components, hooks, services, validation logic, and type definitions.

Cross-cutting concerns such as routing, layouts, theming, and shared UI components are placed in dedicated shared modules.

The frontend follows the same organizational philosophy as the backend, providing a consistent development experience across the entire project.

## Consequences

### Positive

- Improves modularity.
- Makes features easier to locate and maintain.
- Encourages encapsulation.
- Keeps related code together.
- Aligns frontend and backend architecture.
- Simplifies future expansion.

### Negative

- Shared code must be managed carefully.
- Developers must avoid placing feature-specific logic inside shared modules.

These trade-offs are considered acceptable because they improve long-term maintainability.

## Alternatives Considered

### Package by Technical Layer

Example:

- components/
- pages/
- hooks/
- services/

Rejected because related functionality becomes scattered across multiple directories as the project grows.

### Hybrid Organization

A hybrid structure was considered.

Rejected because it introduces ambiguity regarding where new files should be placed and reduces architectural consistency.
