# CareerHub Development Setup

## 1. Purpose

This document describes how to run CareerHub locally for development.

Only the backend is currently implemented. Frontend setup will be added in Phase 4.

---

## 2. Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Git | Any recent version | |
| JDK | 21 | Maven itself is not required; the project includes the Maven Wrapper. |
| Docker | Docker Desktop or Docker Engine with Compose | Used to run PostgreSQL. |

---

## 3. Environment Variables

Configuration is read from a `.env` file in the repository root. The file is ignored by Git.

Create it from the template:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

| Variable | Used by | Description |
|----------|---------|-------------|
| `POSTGRES_DB` | Docker Compose | Database created by the PostgreSQL container. |
| `POSTGRES_USER` | Docker Compose | Database user created by the container. |
| `POSTGRES_PASSWORD` | Docker Compose | Password of that user. |
| `DB_URL` | Backend | JDBC URL. Locally, PostgreSQL is exposed on port `5433`. |
| `DB_USERNAME` | Backend | Must match `POSTGRES_USER`. |
| `DB_PASSWORD` | Backend | Must match `POSTGRES_PASSWORD`. |
| `JWT_SECRET` | Backend | Base64-encoded random key of at least 32 bytes used to sign access tokens. |

### 3.1 Generating the JWT Secret

The secret must be generated with a cryptographically secure random generator. A memorable password is not acceptable.

Linux or macOS:

```bash
openssl rand -base64 32
```

Windows PowerShell:

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

The application refuses to start if the secret is missing, not Base64-encoded or shorter than 32 bytes.

Changing the secret invalidates every issued access token.

---

## 4. Starting the Database

From the repository root:

```bash
docker compose -f docker/docker-compose.yml up -d
```

PostgreSQL is exposed on port `5433` instead of the default `5432`, to avoid conflicts with a locally installed PostgreSQL server.

Flyway applies the database migrations automatically when the backend starts.

---

## 5. Running the Backend

All backend commands must be run from the `backend` directory, because the application loads `../.env` relative to the working directory.

| Task | Linux / macOS | Windows |
|------|---------------|---------|
| Run tests | `./mvnw test` | `.\mvnw.cmd test` |
| Run tests from a clean build | `./mvnw clean test` | `.\mvnw.cmd clean test` |
| Start the application | `./mvnw spring-boot:run` | `.\mvnw.cmd spring-boot:run` |

Run `clean` after moving, renaming or deleting classes, so that stale compiled classes do not remain in `target/`.

The tests run against the PostgreSQL database started in section 4, so it must be running.

The API is available at `http://localhost:8080/api/v1`.

### 5.1 Smoke Test

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"dev@example.com","password":"password123","firstName":"Dev","lastName":"User"}'

curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"dev@example.com","password":"password123"}'
```

Unlike the automated tests, these requests store data in the development database.

---

## 6. Stopping and Resetting the Database

| Task | Command |
|------|---------|
| Stop the container and keep the data | `docker compose -f docker/docker-compose.yml down` |
| Stop the container and delete all data | `docker compose -f docker/docker-compose.yml down -v` |

Deleting the volume recreates the schema from the migrations on the next start.

---

## 7. Continuous Integration

GitHub Actions runs the backend tests on every push to `main` and on every pull request (`.github/workflows/backend-ci.yml`).

The workflow starts a disposable PostgreSQL 17 service, applies the migrations to an empty database and generates a new JWT secret for each run. No `.env` file is used in CI.

---

## 8. Troubleshooting

| Symptom | Cause | Solution |
|---------|-------|----------|
| `Connection to localhost:5433 refused` | PostgreSQL is not running. | Start Docker and the database (section 4). |
| `JWT secret must be configured` or `must be at least 32 bytes` | `JWT_SECRET` is missing or too short. | Generate a new secret (section 3.1). |
| `Could not resolve placeholder` | The `.env` file was not found. | Run the command from the `backend` directory and check that `.env` exists in the repository root. |
| Duplicate entity errors or more tests than expected | Stale classes in `target/`. | Run the tests with `clean`. |
| No response on port `8080` | The application is not running. | Start it with `spring-boot:run` and wait for the startup message. |
