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

## 4.3 JobOffer

### Description

Represents a specific employment opportunity published by a company.

A JobOffer contains information describing the position, working conditions, and other relevant details provided by the employer.

JobOffer is independent from the user's application process. A single offer may be associated with multiple applications belonging to different users, while a JobApplication may exist without an associated JobOffer.

---

### Responsibilities

A JobOffer is responsible for:

- Representing a specific employment opportunity.
- Storing information describing the offered position.
- Providing context for applications associated with the offer.
- Preserving the details of an employment opportunity independently of individual applications.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| title | String | Yes | Title of the offered position. |
| description | String (TEXT) | No | Original description of the job offer. |
| location | String | No | Location associated with the position. |
| workMode | WorkMode | No | Working arrangement, such as remote, hybrid, or on-site. |
| employmentType | EmploymentType | No | Type of employment, such as full-time, part-time, or internship. |
| sourceUrl | String | No | URL of the original job offer. |
| publishedAt | LocalDate | No | Date on which the offer was published. |
| expiresAt | LocalDate | No | Date on which the offer expires, if known. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| Company | Many-to-One | Yes | Company that published the offer. |
| JobApplication | One-to-Many | No | Applications associated with this offer. |

---

### Business Rules

- Every JobOffer belongs to exactly one Company.
- A JobOffer may exist without any JobApplication.
- A JobApplication may reference a JobOffer only if the JobOffer belongs to the same Company as the JobApplication.
- The title of a JobOffer is required.
- An expiration date cannot be earlier than the publication date when both are provided.
- Deleting a JobOffer must not automatically delete associated JobApplications.
- The original JobOffer data should remain independent from changes made to individual JobApplications.

---

### Validation

| Attribute | Rule |
|-----------|------|
| title | Required. Maximum length to be defined during implementation. |
| description | Optional. |
| location | Optional. |
| workMode | Optional. |
| employmentType | Optional. |
| sourceUrl | Must be a valid URL if provided. |
| publishedAt | Optional. |
| expiresAt | Optional. Must not precede publishedAt when both are provided. |

---

### Notes

JobOffer represents the employment opportunity itself rather than a user's interaction with it.

Salary information is intentionally excluded from the MVP to keep the initial data model simple. It may be introduced in a future version if it provides sufficient value.

The original job description is retained as free-form text to preserve useful information for future features such as analytics and AI-assisted job analysis.

## 4.4 Interview

### Description

Represents an interview or interview-related event associated with a JobApplication.

An Interview records the scheduling and relevant details of a recruitment interview, including when it takes place, how it is conducted, and its current status.

An Interview always belongs to a JobApplication and therefore indirectly belongs to the User and Company associated with that application.

---

### Responsibilities

An Interview is responsible for:

- Representing an interview associated with a JobApplication.
- Storing the scheduled date and time.
- Identifying the interview format.
- Tracking the interview's current status.
- Storing relevant information needed by the user before or after the interview.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| scheduledAt | Instant | Yes | Date and time at which the interview is scheduled. |
| type | InterviewType | Yes | Format or type of interview. |
| status | InterviewStatus | Yes | Current status of the interview. |
| location | String | No | Physical location or meeting information. |
| interviewer | String | No | Name or description of the interviewer. |
| notes | String (TEXT) | No | User-defined notes about the interview. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| JobApplication | Many-to-One | Yes | Application associated with the interview. |

---

### Business Rules

- Every Interview belongs to exactly one JobApplication.
- An Interview cannot exist without a JobApplication.
- An Interview must have a scheduled date and time.
- An Interview must have a status.
- An Interview with status `CANCELLED` must not be considered an upcoming interview.
- An Interview with status `COMPLETED` must not be considered an upcoming interview.
- Deleting a JobApplication must not automatically delete its Interviews.
- An Interview cannot be reassigned to a different JobApplication after creation.

---

### Validation

| Attribute | Rule |
|-----------|------|
| scheduledAt | Required. |
| type | Required. |
| status | Required. |
| location | Optional. |
| interviewer | Optional. |
| notes | Optional. |

---

### Notes

Interview is modeled as a separate entity rather than as attributes of JobApplication because a single application may involve multiple interviews.

The MVP focuses on interview tracking and scheduling. Calendar integrations, reminders, notifications, and advanced interview feedback may be introduced in future versions.

The `location` attribute is intentionally modeled as a free-form string in the MVP. It may contain either a physical location or meeting information.

The `interviewer` attribute is also modeled as a free-form string because CareerHub does not currently manage external people as independent domain entities.

## 4.5 Task

### Description

Represents an actionable item that the user needs to complete as part of their professional career management.

A Task may be associated with a specific JobApplication when it relates to a recruitment process, or it may exist independently as a general career-related task.

---

### Responsibilities

A Task is responsible for:

- Representing an actionable item.
- Tracking whether the task has been completed.
- Storing a deadline when one exists.
- Allowing tasks to be prioritized.
- Optionally associating a task with a specific JobApplication.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| title | String | Yes | Short description of the task. |
| description | String (TEXT) | No | Additional information about the task. |
| dueAt | Instant | No | Date and time by which the task should be completed. |
| status | TaskStatus | Yes | Current state of the task. |
| priority | TaskPriority | Yes | User-defined priority. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| User | Many-to-One | Yes | Owner of the task. |
| JobApplication | Many-to-One | No | Application associated with the task, if applicable. |

---

### Business Rules

- Every Task belongs to exactly one User.
- A Task may optionally belong to one JobApplication.
- If a Task is associated with a JobApplication, both must belong to the same User.
- A Task must have a status.
- A Task must have a priority.
- A completed Task must not be considered pending.
- A cancelled Task must not be considered pending.
- Deleting a JobApplication must not automatically delete associated Tasks.
- A Task may exist independently of any JobApplication.

---

### Validation

| Attribute | Rule |
|-----------|------|
| title | Required. Maximum length to be defined during implementation. |
| description | Optional. |
| dueAt | Optional. |
| status | Required. |
| priority | Required. |
| user | Required. |
| jobApplication | Optional. |

---

### Notes

Task is intentionally designed as an independent entity that may optionally reference a JobApplication.

This allows CareerHub to manage both recruitment-specific tasks and general professional tasks without introducing additional task types or polymorphic relationships.

Calendar integration, reminders, recurring tasks, and advanced task management are considered future extensions.

## 4.6 User

### Description

Represents a registered CareerHub user and the owner of the professional data managed within the platform.

A User is the root entity for user-owned career information. Companies, JobApplications, and Tasks are associated with a specific User and must not expose or modify another user's data.

User also contains the credentials required to authenticate with CareerHub.

---

### Responsibilities

A User is responsible for:

- Representing a registered CareerHub account.
- Identifying the owner of career-related data.
- Providing the identity used for authorization.
- Storing the credentials required for authentication.
- Serving as the ownership boundary for user-specific data.

---

### Attributes

| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | Unique identifier inherited from BaseEntity. |
| createdAt | Instant | Yes | Creation timestamp inherited from BaseEntity. |
| updatedAt | Instant | Yes | Last modification timestamp inherited from BaseEntity. |
| email | String | Yes | Unique email address used to identify the account. |
| passwordHash | String | Yes | Securely hashed user password. |
| firstName | String | Yes | User's first name. |
| lastName | String | Yes | User's last name. |

---

### Relationships

| Entity | Cardinality | Required | Description |
|--------|-------------|----------|-------------|
| Company | One-to-Many | No | Companies managed by the user. |
| JobApplication | One-to-Many | No | Job applications created by the user. |
| Task | One-to-Many | No | Tasks owned by the user. |

---

### Business Rules

- Every User must have a unique email address.
- A User's password must never be stored in plain text.
- The password must be stored as a secure one-way hash.
- Every Company belongs to exactly one User.
- Every JobApplication belongs to exactly one User.
- Every Task belongs to exactly one User.
- A User must only be able to access their own career-related data.
- Deleting a User must not be implemented using unrestricted cascading deletion.
- A User cannot be deleted while dependent career data still exists.

---

### Validation

| Attribute | Rule |
|-----------|------|
| email | Required. Must be a valid email address and unique within CareerHub. |
| passwordHash | Required. Must contain a securely hashed password. |
| firstName | Required. Maximum length to be defined during implementation. |
| lastName | Required. Maximum length to be defined during implementation. |

---

### Notes

The User entity represents both the account identity and the ownership boundary of CareerHub data.

Authentication and authorization logic are implemented by the security layer rather than directly by the entity.

Passwords are never stored directly. The application must hash passwords using a secure password-hashing mechanism before persistence.

Future versions may introduce additional profile information, preferences, roles, or account-management features without changing the fundamental ownership model.
