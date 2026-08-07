# ADR-0006: Expose DTOs Instead of Persistence Entities

**Status:** Accepted

## Context

CareerHub exposes a REST API that serves as the communication layer between the backend and the frontend.

Persistence entities are designed to represent the database model and should remain independent from external clients.

Exposing entities directly through the API tightly couples the persistence model to the public contract, making future changes difficult and increasing the risk of unintentionally exposing internal implementation details.

## Decision

CareerHub uses Data Transfer Objects (DTOs) as the exclusive communication model for the REST API.

Persistence entities are never exposed directly to clients.

Each API endpoint accepts request DTOs and returns response DTOs.

Mapping between entities and DTOs is performed within the application layer.

## Consequences

### Positive

- Decouples the REST API from the persistence model.
- Allows the database schema to evolve without breaking the API.
- Prevents accidental exposure of internal fields.
- Reduces payload size by exposing only the required data.
- Enables different representations of the same entity depending on the use case.

### Negative

- Requires additional mapping logic.
- Introduces extra classes for requests and responses.

These trade-offs are considered acceptable because they improve maintainability and provide a clear separation of responsibilities.

## Alternatives Considered

### Expose JPA Entities Directly

Rejected because it tightly couples the API to the persistence model and makes future refactoring significantly more difficult.

It also increases the risk of exposing sensitive or implementation-specific information unintentionally.
