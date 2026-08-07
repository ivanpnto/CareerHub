# Requirements

## 1. Introduction

### 1.1 Purpose

This document defines the functional and non-functional requirements for CareerHub. It serves as the primary specification of the system's behavior and establishes a shared understanding of the product before implementation begins.

### 1.2 Scope

The first version of CareerHub focuses on providing a reliable platform for managing the entire job search process.

The MVP includes user authentication, company management, job offers, applications, interviews, tasks, and a dashboard summarizing the user's activity.

Features such as AI assistance, external integrations, document management, analytics, notifications, and mobile applications are outside the scope of this version.

---

# 2. Functional Requirements

## Authentication

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-100 | Must | The system shall allow users to register using a unique email address and password. |
| FR-101 | Must | The system shall allow registered users to authenticate using their email address and password. |
| FR-102 | Must | The system shall prevent unauthorized access to protected resources. |
| FR-103 | Must | The system shall allow authenticated users to log out. |
| FR-104 | Must | Each authenticated user shall only be able to access and manage their own data. |

---

## Dashboard

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-200 | Must | The system shall provide authenticated users with an overview of their job search activity. |
| FR-201 | Must | The dashboard shall display application statistics. |
| FR-202 | Must | The dashboard shall display company statistics. |
| FR-203 | Must | The dashboard shall display job offer statistics. |
| FR-204 | Must | The dashboard shall display upcoming interviews. |
| FR-205 | Must | The dashboard shall display pending tasks ordered by due date. |

---

## Companies

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-300 | Must | The system shall allow authenticated users to create, view, update and delete companies. |
| FR-301 | Must | A company shall store relevant business information and personal notes. |
| FR-302 | Must | Each company shall belong to exactly one user. |
| FR-303 | Should | The system shall allow users to search companies by name. |

---

## Job Offers

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-400 | Must | The system shall allow authenticated users to create, view, update and delete job offers. |
| FR-401 | Must | Every job offer shall belong to one company. |
| FR-402 | Must | A job offer shall store relevant information such as title, location, source, salary (optional), publication link and notes. |
| FR-403 | Should | The system shall allow users to filter job offers. |

---

## Applications

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-500 | Must | The system shall allow authenticated users to create, view, update and delete applications. |
| FR-501 | Must | Every application shall be associated with one job offer. |
| FR-502 | Must | Every application shall maintain a current status. |
| FR-503 | Must | The system shall allow users to change the application status. |
| FR-504 | Must | Users shall be able to store notes related to each application. |

---

## Interviews

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-600 | Must | The system shall allow authenticated users to create, view, update and delete interviews. |
| FR-601 | Must | Every interview shall belong to one application. |
| FR-602 | Must | An interview shall store its scheduled date and time. |
| FR-603 | Should | Users shall be able to store interview notes. |

---

## Tasks

| ID | Priority | Requirement |
|----|----------|-------------|
| FR-700 | Must | The system shall allow authenticated users to create, view, update and delete tasks. |
| FR-701 | Must | Tasks may optionally be associated with an application. |
| FR-702 | Must | Tasks shall support due dates and completion status. |
| FR-703 | Should | The system shall allow users to prioritize tasks. |

---

# 3. Non-Functional Requirements

## Security

- NFR-001 — Authentication shall be required to access protected resources.
- NFR-002 — User passwords shall never be stored in plain text.
- NFR-003 — Users shall only access their own resources.

## Performance

- NFR-010 — The application should respond to standard user requests within acceptable time under normal usage.
- NFR-011 — Dashboard information should be retrieved efficiently.

## Reliability

- NFR-020 — Data shall remain consistent after CRUD operations.
- NFR-021 — The system shall prevent invalid entity relationships.

## Maintainability

- NFR-030 — The application shall follow a modular architecture.
- NFR-031 — Source code shall follow defined coding conventions.
- NFR-032 — The project shall be documented.

## Usability

- NFR-040 — The interface shall be consistent throughout the application.
- NFR-041 — Navigation shall be intuitive.

---

# 4. Business Rules

| ID | Rule |
|----|------|
| BR-001 | Every company belongs to one user. |
| BR-002 | Every job offer belongs to one company. |
| BR-003 | Every application belongs to one job offer. |
| BR-004 | Every interview belongs to one application. |
| BR-005 | Tasks may optionally belong to one application. |
| BR-006 | Applications must always have exactly one status. |

---

# 5. Assumptions and Constraints

- The MVP is designed for individual users only.
- External integrations are outside the scope.
- Internet connection is required.
- English is the official language of the project.
- The application is designed primarily for desktop web browsers.
