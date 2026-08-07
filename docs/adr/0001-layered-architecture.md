# ADR-0001: Adopt Layered Architecture

**Status:** Accepted

## Context

CareerHub is being developed as a long-term software product rather than a short-lived demonstration project.

The application requires a clear separation between presentation, business logic, and data access in order to remain maintainable as new features are introduced.

Several architectural styles were considered, including Layered Architecture, Hexagonal Architecture, and Clean Architecture.

While more advanced architectures provide greater flexibility, they also introduce additional complexity that is not currently justified by the project's size and requirements.

## Decision

CareerHub adopts a Layered Architecture.

The backend is organized into the following logical layers:

- Presentation Layer
- Business Layer
- Persistence Layer

Each layer has a clearly defined responsibility and communicates only with adjacent layers.

Business logic resides exclusively in the service layer.

Repositories are responsible only for data access.

Controllers expose the REST API without containing business logic.

## Consequences

### Positive

- Simple and widely understood architecture.
- Excellent integration with Spring Boot.
- Easy for new contributors to understand.
- Clear separation of responsibilities.
- Straightforward testing strategy.
- Can evolve incrementally as the project grows.

### Negative

- Less flexible than Hexagonal or Clean Architecture.
- Business logic depends on the Spring application structure.
- Future migration to another architectural style would require refactoring.

These drawbacks are considered acceptable given the project's current scope.

## Alternatives Considered

### Hexagonal Architecture

Rejected because it introduces additional abstraction layers that are unnecessary for the initial scope of the project.

### Clean Architecture

Rejected because it increases complexity without providing sufficient practical benefits for the MVP.

The project may adopt selected ideas from these architectures in the future if increasing complexity justifies the change.
