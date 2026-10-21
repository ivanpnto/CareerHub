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
```

This endpoint is public.

| Field | Type | Required | Rules |
|-------|------|----------|-------|
| email | string | Yes | Valid email address. Maximum 255 characters. Trimmed and converted to lowercase before storage. Must not already be registered (case-insensitive). |
| password | string | Yes | Between 8 and 64 characters. Must not exceed 72 bytes when encoded as UTF-8. |
| firstName | string | Yes | Maximum 100 characters. |
| lastName | string | Yes | Maximum 100 characters. |

#### Response — `201 Created`

```json
{
  "id": "429e373f-3dc5-4568-8059-aa8edacd1430",
  "email": "ivan@example.com",
  "firstName": "Ivan",
  "lastName": "Pinto"
}
```

The response never contains the password or its hash.

No `Location` header is returned, because no endpoint exposes users by identifier yet.

#### Errors

| Status | Condition |
|--------|-----------|
| `400 Bad Request` | The body is malformed or fails validation. |
| `409 Conflict` | The email address is already registered. |

The remaining authentication endpoints will be documented as they are implemented.

---

## 6. Error Responses

Error responses follow RFC 9457 (Problem Details for HTTP APIs) and use the following content type:

    Content-Type: application/problem+json

Every error response contains at least `title`, `status` and `detail`. Additional standard members, such as `type` and `instance`, may be present.

### 6.1 Validation Errors

Validation failures return `400 Bad Request` with an `errors` object that maps each invalid field to a message:

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Request validation failed.",
  "errors": {
    "email": "must be a well-formed email address",
    "password": "size must be between 8 and 64"
  }
}
```

Messages are human-readable and may be localized according to the request locale. Clients must rely on field names, not on message text.

Rejected values are never included in the response.

### 6.2 Status Codes

| Status | Meaning |
|--------|---------|
| `400 Bad Request` | The request is malformed or fails validation. |
| `401 Unauthorized` | Authentication is required. This response currently has an empty body. |
| `404 Not Found` | The resource does not exist or belongs to another user. |
| `409 Conflict` | The request conflicts with existing data. |
| `500 Internal Server Error` | An unexpected error occurred. Internal details are never exposed. |
