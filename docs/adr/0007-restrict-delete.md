# ADR-0007: Adopt a Restrictive Delete Policy

**Status:** Accepted

## Context

CareerHub manages information that represents the user's professional history, including applications, interviews, tasks, and companies.

Accidental deletion of related records could lead to irreversible data loss and compromise the consistency of the user's career tracking.

The project requires a deletion strategy that prioritizes data integrity over convenience.

## Decision

CareerHub adopts a restrictive deletion policy.

Foreign key relationships prevent the deletion of entities while dependent records still exist.

Deletion operations must be handled explicitly by the application.

Cascade delete operations are avoided by default and are only considered when they accurately reflect ownership within the domain model.

## Consequences

### Positive

- Prevents accidental data loss.
- Preserves referential integrity.
- Makes deletion operations explicit and predictable.
- Encourages careful handling of business rules.

### Negative

- Delete operations require additional validation.
- Users may need to remove dependent records before deleting a parent entity.

These trade-offs are considered acceptable because preserving user data is a higher priority than simplifying delete operations.

## Alternatives Considered

### Cascade Delete

Rejected because it may remove valuable historical information unintentionally.

Automatic cascading can hide the impact of deletion operations and increase the risk of accidental data loss.

### Soft Delete

Considered for future versions.

Rejected for the MVP because it introduces additional complexity without solving an immediate business requirement.

The architecture remains compatible with introducing soft deletion in the future if justified.
