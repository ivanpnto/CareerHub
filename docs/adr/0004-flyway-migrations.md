# ADR-0004: Use Flyway for Database Schema Migrations

**Status:** Accepted

## Context

CareerHub requires a reliable and reproducible way to manage database schema evolution throughout the project's lifetime.

Relying on automatic schema generation provided by the ORM is convenient during early development but does not provide version control, traceability, or reproducibility across different environments.

As the project is intended to evolve over several years, database changes must be managed in a predictable and controlled manner.

## Decision

CareerHub uses Flyway as the database migration tool.

Every structural change to the database schema is introduced through a new versioned migration.

Migration files are treated as immutable once they have been applied.

The database schema is therefore managed as part of the application's source code.

## Consequences

### Positive

- Every schema change is version-controlled.
- Development, testing, and production environments remain consistent.
- The complete history of the database schema is preserved.
- New developers can recreate the database automatically.
- Database evolution becomes predictable and reproducible.

### Negative

- Every schema modification requires creating a new migration.
- Developers must understand the migration lifecycle.
- Mistakes in migrations require corrective migrations instead of editing existing ones.

These trade-offs are considered acceptable in exchange for improved reliability and maintainability.

## Alternatives Considered

### Hibernate Automatic Schema Generation

Using `spring.jpa.hibernate.ddl-auto=update` was considered because it simplifies early development.

Rejected because it does not provide schema versioning, migration history, or reproducible database evolution.

### Manual SQL Scripts

Maintaining SQL scripts without a migration framework was considered.

Rejected because it makes schema versioning more difficult and increases the risk of inconsistencies between environments.
