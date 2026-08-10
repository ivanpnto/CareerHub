# CareerHub Roadmap

## 1. Purpose

This document defines the planned evolution of CareerHub from its initial development stage to future versions of the product.

The roadmap provides a high-level development plan without prescribing individual implementation tasks.

It is intended to:

- Define development phases.
- Establish priorities.
- Separate MVP functionality from future features.
- Provide a clear progression for the project.
- Allow the roadmap to evolve as the product and technical requirements change.

The roadmap is not a fixed schedule. Priorities and implementation details may change as development progresses and new requirements are identified.

---

## 2. Development Strategy

CareerHub will be developed incrementally using a feature-oriented approach.

Development will prioritize delivering complete, functional vertical slices rather than implementing entire technical layers independently.

A typical feature will progress through the following stages:

1. Domain and design review.
2. Backend implementation.
3. Backend testing.
4. Frontend implementation.
5. Frontend and backend integration.
6. End-to-end verification.
7. Documentation update.

This approach ensures that each completed feature provides a functional increment of the product.

### Development Principles

The development process will follow these principles:

- Implement the smallest useful increment first.
- Avoid premature implementation of future features.
- Keep architectural decisions documented.
- Write tests alongside functionality.
- Keep the frontend and backend contracts explicit.
- Refactor when technical debt becomes significant.
- Prefer maintainability over implementation speed.
- Keep the MVP intentionally limited in scope.

---

## 3. Phase 0 — Project Foundation

### Objective

Establish the repository structure, development environment, tooling, and project conventions required before implementing application functionality.

### Planned Work

- Initialize the Git repository.
- Configure the repository structure.
- Create backend and frontend projects.
- Configure Java 21 and Spring Boot.
- Configure React, TypeScript, and Vite.
- Configure Material UI.
- Configure PostgreSQL.
- Configure Docker and Docker Compose.
- Configure environment variables.
- Configure Git conventions.
- Configure Conventional Commits.
- Configure initial GitHub Actions workflow.
- Establish project documentation structure.
- Configure basic code quality and formatting tools.

### Expected Outcome

At the end of Phase 0:

- The repository can be cloned and started by another developer.
- Backend and frontend applications can run locally.
- PostgreSQL can be started through Docker Compose.
- The basic development workflow is documented.
- The project has a reproducible development environment.
- CI can execute the initial project checks.

## 4. Phase 1 — Backend Foundation

### Objective

Establish the backend architecture, persistence layer, database migration system, security foundation, and development conventions required by the application.

### Planned Work

- Implement the layered architecture.
- Establish the feature-oriented package structure.
- Configure Spring Boot application settings.
- Configure PostgreSQL connectivity.
- Integrate Flyway for database migrations.
- Implement the shared `BaseEntity`.
- Configure JPA and Hibernate.
- Establish repository conventions.
- Establish service conventions.
- Establish controller conventions.
- Configure global exception handling.
- Implement common API error responses.
- Configure backend testing infrastructure.
- Configure Spring Security foundation.
- Document relevant implementation decisions.

### Expected Outcome

At the end of Phase 1:

- The backend follows the architecture defined in `architecture.md`.
- The application can connect to PostgreSQL.
- Database schema changes are managed through Flyway.
- Shared persistence functionality is established.
- API errors follow a consistent structure.
- Backend tests can be executed independently.
- The security infrastructure is ready for authentication implementation.

---

## 5. Phase 2 — Authentication

### Objective

Implement the authentication and account-management functionality required to securely identify CareerHub users.

### Planned Work

- Implement User domain model.
- Implement user persistence.
- Implement user registration.
- Implement password hashing.
- Implement login.
- Implement authentication tokens.
- Implement authentication filters and security configuration.
- Implement logout.
- Implement authentication-related validation.
- Implement authentication error handling.
- Implement unit and integration tests for authentication.

### Expected Outcome

At the end of Phase 2:

- Users can create CareerHub accounts.
- Users can authenticate securely.
- Passwords are never stored in plain text.
- Protected API endpoints require authentication.
- Authenticated users are correctly identified by the backend.
- Authentication behavior is covered by automated tests.

---

## 6. Phase 3 — Core Domain

### Objective

Implement the core CareerHub domain entities and their associated business logic.

### Planned Work

Implement the following resources incrementally:

1. Company
2. JobOffer
3. JobApplication
4. Interview
5. Task

For each resource:

- Implement persistence model.
- Create Flyway migration.
- Implement repository.
- Implement service layer.
- Implement request and response DTOs.
- Implement controller.
- Implement validation.
- Implement authorization and ownership checks.
- Implement unit tests.
- Implement integration tests.
- Verify API behavior.

### Expected Outcome

At the end of Phase 3:

- Users can manage companies.
- Users can manage job offers.
- Users can manage job applications.
- Users can manage interviews.
- Users can manage tasks.
- Domain relationships and business rules are enforced by the backend.
- Users cannot access another user's data.
- Core API functionality is covered by automated tests.

---

## 7. Phase 4 — Frontend Foundation

### Objective

Establish the frontend architecture and reusable UI infrastructure required to build the CareerHub interface.

### Planned Work

- Configure React and TypeScript.
- Establish feature-oriented frontend structure.
- Configure Material UI.
- Configure application routing.
- Establish API client infrastructure.
- Establish authentication state management.
- Create reusable layout components.
- Create reusable form components.
- Create reusable feedback components.
- Establish frontend validation conventions.
- Configure frontend testing infrastructure.
- Establish frontend error-handling conventions.

### Expected Outcome

At the end of Phase 4:

- The frontend follows the architectural conventions defined for CareerHub.
- The application has a consistent visual foundation.
- Authentication state can be managed.
- Frontend features can communicate with the backend through a consistent API layer.
- Reusable UI components are available for future features.

---

## 8. Phase 5 — MVP User Experience

### Objective

Connect the frontend and backend into a complete usable CareerHub application.

### Planned Work

- Implement registration interface.
- Implement login interface.
- Implement logout flow.
- Implement protected application routes.
- Implement dashboard.
- Implement company management UI.
- Implement job offer management UI.
- Implement application management UI.
- Implement interview management UI.
- Implement task management UI.
- Implement application status changes.
- Implement upcoming interviews and tasks on the dashboard.
- Implement loading, empty, and error states.
- Verify responsive behavior.

### Expected Outcome

At the end of Phase 5:

A user can:

1. Create an account.
2. Log in.
3. Create and manage companies.
4. Create and manage job offers.
5. Create and manage applications.
6. Track application status.
7. Create and manage interviews.
8. Create and manage tasks.
9. View relevant information from the dashboard.
10. Log out.

The complete MVP workflow is functional from the user interface to the database.

---

## 9. Phase 6 — Testing & Quality

### Objective

Ensure that the MVP is reliable, maintainable, and suitable for public release.

### Planned Work

- Review backend unit test coverage.
- Review backend integration tests.
- Add frontend component tests where appropriate.
- Test authentication and authorization boundaries.
- Test validation and error handling.
- Test important domain business rules.
- Verify database migrations from a clean environment.
- Review API contracts.
- Review frontend and backend code quality.
- Identify and address significant technical debt.
- Review Docker setup.
- Review CI pipeline.
- Perform manual end-to-end testing.

### Expected Outcome

At the end of Phase 6:

- Critical business logic is covered by automated tests.
- Authentication and authorization boundaries are verified.
- The application can be built and tested through CI.
- Database migrations work from a clean database.
- Significant known technical debt has been addressed.
- The MVP is considered stable enough for release.

---

## 10. Phase 7 — MVP Release

### Objective

Prepare and publish the first complete version of CareerHub.

### Planned Work

- Finalize README.
- Add application screenshots.
- Document local development setup.
- Document architecture.
- Document API.
- Document database design.
- Update changelog.
- Review Git history and commit quality.
- Create the MVP release.
- Tag the release in Git.
- Publish the project repository as a portfolio project.

### Expected Outcome

CareerHub MVP is publicly available as a complete, documented, tested, and reproducible software project.

The MVP should provide a coherent end-to-end experience rather than simply demonstrating isolated technical components.

## 11. Future Versions

Future development will be driven by actual usage of CareerHub and validated user needs.

Features listed below are planned directions rather than fixed implementation commitments.

### Version 2 — Professional Organization

Potential features:

- Document management.
- CV management.
- Cover letter management.
- Calendar integration.
- Notifications.
- Reminders.
- Improved task management.

### Version 3 — Analytics & Insights

Potential features:

- Advanced dashboard.
- Application statistics.
- Interview statistics.
- Application funnel.
- Timeline.
- Progress tracking.
- Career-related analytics.

### Version 4 — Artificial Intelligence

Potential features:

- CV and job offer comparison.
- Job requirement analysis.
- Skill extraction.
- Skill gap analysis.
- Personalized recommendations.
- Cover letter generation.
- AI-assisted career insights.

AI features must provide meaningful value within CareerHub's workflows.

AI must not be introduced simply as a generic interface for interacting with a language model.

---

## 12. Roadmap Principles

Future development should follow the following principles:

- New features must solve a real user problem.
- Features should not be added solely to increase the project's technical complexity.
- Architectural changes should be justified by actual requirements.
- Existing functionality should remain stable when possible.
- Technical debt should be addressed continuously.
- Major architectural decisions should be documented through ADRs.
- The roadmap should be reviewed periodically as CareerHub evolves.
