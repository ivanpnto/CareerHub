# Domain Model

## 1. Domain Overview

CareerHub models the job search process as a collection of interconnected domain entities.

The central concept of the system is the Application, which represents a user's decision to apply for a specific job offer.

Around each application, users may organize interviews, tasks, notes, and progress throughout the recruitment process.

Companies and job offers provide context, while the dashboard offers a summarized view of the current state of the user's job search.

Each authenticated user owns an independent workspace, ensuring complete isolation of personal data.

## 2. Core Entities

### User

Represents an authenticated person using CareerHub.

A user owns all the information stored in the system, including companies, job offers, applications, interviews, and tasks.

Users are completely isolated from one another, and no data is shared between accounts.

The user represents the owner of a personal workspace that contains all career-related information.

### Company

Represents an organization in which the user has an interest.

A company provides organizational context for the user's job search and may contain multiple job offers and applications.

Companies are owned by a single user.

### Job Offer

Represents a specific employment opportunity offered by a company.

A job offer belongs to a single user and is associated with exactly one company.

Job offers provide additional context for applications but are optional, allowing the system to support spontaneous applications and other recruitment scenarios.

### Application

Represents the user's decision to apply for, or actively pursue, an employment opportunity.

Application is the central entity of the domain and tracks the progress of the recruitment process.

Every application belongs to one user and one company.

An application may optionally reference a specific job offer.

### Interview

Represents a scheduled interaction between the user and a company during the recruitment process.

Each interview belongs to one user and exactly one application.

An application may contain multiple interviews representing different recruitment stages.

### Task

Represents an actionable item that helps the user progress through the job search process.

Every task belongs to one user.

Tasks may optionally be associated with an application.

Tasks without an associated application represent general career-related activities.

## 3. Relationships

All domain entities belong to a single user.

The relationships described below represent how entities interact within the user's personal workspace.

### User → Company

A user may manage multiple companies.

Each company belongs to exactly one user.

---

### Company → Job Offer

A company may contain multiple job offers.

Each job offer belongs to exactly one company.

---

### Company → Application

A company may have multiple applications associated with it.

Each application belongs to exactly one company.

---

### Job Offer → Application

A job offer may be associated with multiple applications.

Associating an application with a job offer is optional.

If an application references a job offer, both entities must belong to the same company.

---

### Application → Interview

An application may contain multiple interviews representing different stages of the recruitment process.

Each interview belongs to exactly one application.

---

### Application → Task

An application may contain multiple tasks.

Associating a task with an application is optional.

Tasks without an associated application represent general career-related activities.

## 4. Entity Lifecycles

### Company

A company is created when the user decides to track an organization of interest.

A company may exist without job offers or applications.

A company may be archived or removed by the user.

---

### Job Offer

A job offer is created when the user identifies a specific employment opportunity.

A job offer always belongs to one company.

A job offer may exist without any associated applications.

---

### Application

An application is created when the user begins a recruitment process with a company.

Every application belongs to one company.

Associating an application with a job offer is optional.

During its lifetime, an application progresses through different recruitment stages represented by its status.

---

### Interview

An interview is created as part of an application's recruitment process.

An application may contain multiple interviews.

Interviews cannot exist without an application.

---

### Task

A task is created whenever the user needs to perform an action related to their career.

Tasks may exist independently or be associated with an application.

Tasks remain available until completed or deleted by the user.

## 4. Entity Lifecycles

### Company

A company is created when the user decides to track an organization of interest.

A company may exist without associated job offers or applications.

A company remains part of the user's workspace until it is explicitly removed.

---

### Job Offer

A job offer is created when the user identifies a specific employment opportunity.

A job offer always belongs to one company.

A job offer may exist without any associated applications.

---

### Application

An application is created when the user starts or intends to start a recruitment process with a company.

Every application belongs to exactly one company.

Associating an application with a specific job offer is optional.

During its lifetime, an application progresses through a series of recruitment stages represented by its status.

---

### Interview

An interview is created as part of an application's recruitment process.

An application may contain multiple interviews.

An interview cannot exist without an associated application.

---

### Task

A task is created whenever the user needs to perform an action related to their career.

Tasks may exist independently or be associated with an application.

Tasks remain available until they are completed or removed.

## 5. Business Invariants

### BI-001 — User Isolation

All domain entities belong to exactly one user.

Users must never access or modify resources owned by another user.

---

### BI-002 — Company Consistency

If an application references a job offer, both entities must belong to the same company.

---

### BI-003 — Referential Integrity

The system shall prevent the deletion of a domain entity while it is still referenced by other domain entities.

Referenced entities must be removed or reassigned before the parent entity can be deleted.

---

### BI-004 — Application Status

Every application must always have exactly one valid status.

Application status transitions shall follow the defined recruitment workflow.

---

### BI-005 — Interview Integrity

An interview cannot exist without an associated application.

