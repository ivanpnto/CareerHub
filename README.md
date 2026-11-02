# CareerHub

[![Backend CI](https://github.com/ivanpnto/CareerHub/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/ivanpnto/CareerHub/actions/workflows/backend-ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Organize your entire job search in one place:** companies, job offers, applications, interviews and tasks.

> 🚧 **Work in progress.** The backend is under active development and the frontend has not started yet. See the [roadmap](docs/roadmap.md).

## Why CareerHub?

Looking for a job involves much more than sending applications. Companies, offers, application statuses, interviews, deadlines and notes usually end up scattered across spreadsheets, emails, calendars and several job boards.

CareerHub centralizes that information in a single structured workspace for students and early-career developers.

## Features (MVP)

| Area | Status |
|---|---|
| Registration and login with JWT access tokens | ✅ Implemented |
| Companies | 🟡 Domain, persistence and service layer implemented; REST API in progress |
| Job offers, applications, interviews and tasks | 🟡 Domain model and persistence implemented |
| Dashboard | ⏳ Planned |
| Web frontend (React) | ⏳ Planned |

Functional and non-functional requirements: [docs/requirements.md](docs/requirements.md).

## Tech stack

| Layer | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 4, Spring Web MVC, Spring Security (OAuth2 Resource Server with JWT), Spring Data JPA / Hibernate, Bean Validation |
| Database | PostgreSQL 17, Flyway migrations |
| Testing & CI | JUnit 5, Spring Boot integration tests (MockMvc, Data JPA) against PostgreSQL, GitHub Actions with a disposable PostgreSQL service |
| Tooling | Maven Wrapper, Docker Compose |
| Frontend (planned) | React |

## Architecture

CareerHub follows a client–server architecture. A React frontend (planned) consumes a versioned REST API (`/api/v1`) exposed by a Spring Boot backend, which owns all business rules, validation and persistence.

- **Layered, package-by-feature backend:** code is grouped by feature (`auth`, `user`, `company`, `joboffer`, `jobapplication`, `interview`, `task`) rather than by technical layer. Each feature package collects its entity, repository, service and controller as that feature is implemented. Cross-cutting concerns live in `shared/`.
- **DTO boundary:** the REST API only accepts and returns DTOs, never JPA entities.
- **UUID primary keys** and **versioned Flyway migrations**.
- **Stateless authentication** with short-lived JWT access tokens. Users can only access their own data.
- **Consistent error responses** through a global exception handler based on Problem Details.

Key decisions are recorded as Architecture Decision Records (ADRs):

| ADR | Decision |
|---|---|
| [0001](docs/adr/0001-layered-architecture.md) | Layered architecture |
| [0002](docs/adr/0002-package-by-feature.md) | Package by feature |
| [0003](docs/adr/0003-uuid-primary-keys.md) | UUID primary keys |
| [0004](docs/adr/0004-flyway-migrations.md) | Flyway migrations |
| [0005](docs/adr/0005-application-centric-domain-model.md) | Application-centric domain model |
| [0006](docs/adr/0006-dto-boundary.md) | DTO boundary |
| [0007](docs/adr/0007-restrict-delete.md) | Restricted deletes |
| [0008](docs/adr/0008-feature-oriented-frontend.md) | Feature-oriented frontend |

## Getting started

**Prerequisites:** JDK 21 and Docker (with Docker Compose). Maven is not required; the project ships with the Maven Wrapper.

```bash
git clone https://github.com/ivanpnto/CareerHub.git
cd CareerHub
cp .env.example .env    # then set JWT_SECRET (see docs/development-setup.md)
docker compose -f docker/docker-compose.yml up -d
cd backend
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080/api/v1`.

To run the tests (PostgreSQL must be running):

```bash
./mvnw test
```

Windows commands, the smoke test and troubleshooting are in [docs/development-setup.md](docs/development-setup.md).

## Project structure

```
CareerHub/
├── backend/             Spring Boot application and tests
├── docker/              Docker Compose file for PostgreSQL
├── docs/                Product and technical documentation, ADRs
└── .github/workflows/   CI pipeline
```

## Documentation

| Document | Content |
|---|---|
| [Vision](docs/vision.md) | Product vision and target users |
| [Requirements](docs/requirements.md) | Functional and non-functional requirements |
| [Architecture](docs/architecture.md) | System and backend architecture |
| [Domain model](docs/domain-model.md) · [Data model](docs/data-model.md) · [Database design](docs/database-design.md) | Domain entities, schema and constraints |
| [API reference](docs/api-reference.md) | REST endpoints and error format |
| [Decision log](docs/decision-log.md) | History of design decisions |
| [Roadmap](docs/roadmap.md) | Development phases and future versions |

## Roadmap

Development is incremental and delivers complete vertical slices: backend, tests, frontend and documentation for each feature.

- [x] Phase 0: Project foundation
- [x] Phase 1: Backend foundation
- [x] Phase 2: Authentication
- [ ] Phase 3: Core domain *(in progress)*
- [ ] Phase 4: Frontend foundation
- [ ] Phase 5: MVP user experience
- [ ] Phase 6: Testing and quality
- [ ] Phase 7: MVP release

Later versions are planned to add professional organization features, analytics and AI-assisted features. See the [roadmap](docs/roadmap.md).

## License

Released under the [MIT License](LICENSE).
