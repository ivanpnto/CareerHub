# Data Model

## 1. Purpose

This document defines the logical structure of the domain entities that compose CareerHub.

It specifies the attributes, relationships, validation rules, and lifecycle considerations for each entity independently of its implementation.

The purpose of this document is to provide a single source of truth for the application's data model before implementation begins.

This model serves as the foundation for the database schema, REST API, and business logic.

## 2. Modeling Principles

The following principles guide the design of every entity within CareerHub.

- Every entity has a single responsibility.
- Every entity is uniquely identified by a UUID.
- Business rules are enforced by the application layer.
- Entities model business concepts rather than database structures.
- Relationships reflect the domain model and avoid unnecessary complexity.
- Shared auditing fields are present in every persistent entity.
- Optional relationships are used only when they represent real business scenarios.

## 3. Shared Entity Attributes

All persistent entities include the following common attributes.

| Attribute | Type | Description |
|----------|------|-------------|
| id | UUID | Unique identifier. |
| createdAt | Instant | Creation timestamp. |
| updatedAt | Instant | Last modification timestamp. |

These attributes are managed automatically by the application and are not intended to be modified directly by clients.

## 4. Entity Specifications

This section defines every entity that composes the CareerHub domain model.

Each entity is described independently, including its purpose, responsibilities, attributes, relationships, business rules and validation constraints.

The entities are presented following the application's domain hierarchy rather than alphabetical order.

Since `JobApplication` is the central entity of CareerHub, it is documented first.

Each entity follows the same structure to ensure consistency throughout the document.

## 4.1 JobApplication

### Description

Represents a user's application for a specific position at a company.

JobApplication is the central entity of CareerHub and acts as the primary record that tracks the user's recruitment process.

It connects the different stages of the hiring journey, including interviews, tasks, notes, and application status, while preserving the historical context of the application independently of future changes to the associated job offer.

---

### Responsibilities

A JobApplication is responsible for:

- Representing a user's application for a specific position.
- Tracking the progress of the recruitment process.
- Preserving the position title at the time of application.
- Acting as the central entity for interviews and tasks.
- Storing user-specific information related to the application.
- Providing historical context even if the associated job offer changes or is removed.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| positionTitle | String | Yes | Snapshot of the position title at the time of application. |
| status | ApplicationStatus | Yes | Current stage of the recruitment process. |
| appliedAt | LocalDate | Yes | Date when the application was submitted. |
| priority | ApplicationPriority | No | User-defined priority for the application. |
| notes | String (TEXT) | No | Free-form notes related to the application. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| User | Many-to-One | Yes | Owner of the application. |
| Company | Many-to-One | Yes | Company where the user applied. |
| JobOffer | Many-to-One | No | Original job offer associated with the application, if available. |
| Interview | One-to-Many | No | Interviews scheduled for this application. |
| Task | One-to-Many | No | Tasks associated with this application. |

---

### Business Rules

- Every JobApplication belongs to exactly one User.
- Every JobApplication belongs to exactly one Company.
- A JobApplication may reference one JobOffer.
- If a JobOffer is associated, it must belong to the same Company as the JobApplication.
- The positionTitle must always preserve the original position applied for.
- The status represents the current stage of the recruitment process.
- Interviews cannot exist without an associated JobApplication.
- Tasks related to the recruitment process should reference a JobApplication.

---

### Validation

| Attribute | Rule |
|-----------|------|
| positionTitle | Required. Maximum length to be defined during implementation. |
| appliedAt | Cannot be null. |
| status | Cannot be null. |
| company | Cannot be null. |
| user | Cannot be null. |

---

### Notes

The JobApplication entity is the core of the CareerHub domain model.

Most future features—including analytics, AI-assisted recommendations, document management, and recruitment timelines—will revolve around this entity.

## 4.2 Company

### Description

Represents an organization to which a user may apply for one or more job positions.

A Company provides the organizational context for job applications and job offers but does not represent the user's recruitment progress itself.

The same company may be associated with multiple job offers and multiple job applications over time.

---

### Responsibilities

A Company is responsible for:

- Representing an employer or organization.
- Providing context for job applications.
- Grouping job offers published by the same organization.
- Serving as a reusable entity across multiple applications.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| name | String | Yes | Official company name. |
| website | String | No | Company's official website. |
| industry | String | No | Industry or business sector. |
| headquarters | String | No | Company's headquarters or main location. |
| notes | String (TEXT) | No | User-defined notes about the company. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| User | Many-to-One | Yes | Owner of the company record. |
| JobOffer | One-to-Many | No | Job offers associated with the company. |
| JobApplication | One-to-Many | No | Applications submitted to the company. |

---

### Business Rules

- Every Company belongs to exactly one User.
- Company names are not required to be globally unique.
- A Company may exist without any job offers.
- A Company may exist without any job applications.
- A Company cannot be deleted while related JobOffers or JobApplications exist.

---

### Validation

| Attribute | Rule |
|-----------|------|
| name | Required. Maximum length to be defined during implementation. |
| website | Must be a valid URL if provided. |
| industry | Optional. |
| headquarters | Optional. |

---

### Notes

A Company represents an organization independently of any specific recruitment process.

It acts as reusable contextual information that can be shared across multiple job offers and applications.
