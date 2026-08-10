# CareerHub API Reference

## 1. Purpose

This document defines the public REST API contract of CareerHub.

It describes the available resources, endpoints, request and response structures, authentication requirements, validation rules, error handling, and general API conventions.

The API is designed to provide a stable contract between the CareerHub frontend and backend while keeping the API layer independent from the persistence model.

---

## 2. API Principles

The CareerHub API follows the following principles:

- RESTful resource-oriented design.
- Versioned endpoints.
- JSON request and response bodies.
- Stateless requests.
- DTO-based request and response models.
- Consistent HTTP status codes.
- Consistent error responses.
- Validation at the API boundary.
- Authentication and authorization enforced by the backend.
- Users can only access resources they own.
- API contracts must not expose persistence entities directly.

The API should prioritize clarity and consistency over unnecessary abstraction.

---

## 3. Base URL & Versioning

All API endpoints are versioned using the following prefix:

    /api/v1

Examples:

    /api/v1/companies
    /api/v1/applications
    /api/v1/interviews

The API version is part of the URL to allow future versions to evolve without immediately breaking existing clients.

Breaking changes should require a new API version.

Non-breaking changes may be introduced within the existing version.

---

## 4. Content Type

The API uses JSON for request and response bodies.

Requests containing a body should use:

    Content-Type: application/json

Successful responses containing a body should return:

    Content-Type: application/json

---

## 5. Authentication

CareerHub uses token-based authentication.

Authentication endpoints are publicly accessible, while protected resources require a valid authentication token.

### 5.1 Register

    POST /api/v1/auth/register

Creates a new CareerHub user account.

#### Request

```json
{
  "email": "ivan@example.com",
  "password": "securePassword",
  "firstName": "Ivan",
  "lastName": "Pinto"
}
