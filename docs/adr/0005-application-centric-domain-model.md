# ADR-0005: Adopt an Application-Centric Domain Model

**Status:** Accepted

## Context

CareerHub is designed to help users manage their job search process rather than simply collect companies or job offers.

Several domain models were considered, including company-centric and job-offer-centric approaches.

The chosen domain model must accurately represent the user's workflow while remaining flexible enough to support future features such as interviews, documents, analytics, and AI-assisted recommendations.

## Decision

CareerHub adopts an application-centric domain model.

`JobApplication` is the central entity of the system.

Every major activity performed by the user is associated with a job application whenever possible.

Companies provide context for applications.

Job offers describe the opportunity being pursued.

Interviews, notes, and many tasks are linked directly to a specific application.

This decision reflects the primary goal of the platform: managing the user's recruitment journey rather than cataloguing companies or vacancies.

## Consequences

### Positive

- The domain model closely matches the user's real workflow.
- Related information is naturally grouped around a single entity.
- Future features can be added without restructuring the domain.
- The dashboard can present meaningful progress based on applications rather than isolated entities.
- The application lifecycle becomes the central business process.

### Negative

- Some information may exist without an associated application (for example, bookmarked companies or saved job offers).

This is intentionally supported through optional relationships where appropriate.

## Alternatives Considered

### Company-Centric Model

Rejected because companies represent context rather than the user's actual activity.

### Job Offer-Centric Model

Rejected because users interact with applications rather than job offers.

Multiple applications may exist for the same company, and not every application necessarily originates from a published job offer.

The application-centric model most accurately represents the domain and provides the best foundation for future evolution.
