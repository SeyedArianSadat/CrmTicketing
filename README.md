# CRM Ticketing System

A full-stack customer relationship and support ticketing backend built with **Spring Boot 4**, **JWT authentication**, **role-based access control**, and a **Thymeleaf** admin dashboard. Supports both an in-memory H2 database for local development and MySQL for production/Docker deployment.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started — Local (H2)](#getting-started--local-h2)
- [Getting Started — Docker (MySQL)](#getting-started--docker-mysql)
- [Default Credentials](#default-credentials)
- [API Overview](#api-overview)
- [Authentication Flow](#authentication-flow)
- [Roles and Permissions](#roles-and-permissions)
- [Swagger UI](#swagger-ui)
- [H2 Console](#h2-console)
- [Actuator & Monitoring](#actuator--monitoring)
- [Environment Variables](#environment-variables)
- [Running Tests](#running-tests)
- [Common Issues](#common-issues)

---

## Features

- JWT-based stateless authentication with access and refresh tokens
- Role-based access control (SUPER_ADMIN, ADMIN, USER, GUEST)
- Fine-grained permission system assigned per role
- Full ticket lifecycle management with history tracking
- Customer and customer request management
- Support agent and department assignment
- SLA (Service Level Agreement) definition per priority level
- File attachment support per ticket
- Internal messaging system per ticket and customer request
- Soft delete with optimistic locking across all entities
- Thymeleaf admin dashboard for user management
- Swagger/OpenAPI documentation on every endpoint
- Spring Actuator health and metrics endpoints
- Prometheus metrics export
- Docker Compose setup for one-command production deployment

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Security | Spring Security + JJWT 0.12.6 |
| Persistence | Spring Data JPA / Hibernate |
| Database (dev) | H2 in-memory (MySQL mode) |
| Database (prod) | MySQL 8.0 |
| DTO Mapping | MapStruct 1.5.5 |
| Boilerplate | Lombok 1.18.30 |
| API Docs | SpringDoc OpenAPI 2.8.0 |
| Templates | Thymeleaf |
| Monitoring | Spring Actuator + Micrometer + Prometheus |
| Build | Maven 3.9 |
| Containerization | Docker + Docker Compose |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/company/crmticketing/
│   │   ├── config/             # Jackson, OpenAPI configuration
│   │   ├── controller/         # REST controllers + Thymeleaf web controllers
│   │   │   └── rest/           # Domain REST controllers
│   │   ├── dto/                # Request/response DTOs per domain
│   │   ├── exception/          # Custom exceptions + GlobalExceptionHandler
│   │   ├── mapper/             # MapStruct mappers
│   │   ├── model/              # JPA entities + enums
│   │   ├── repository/         # Spring Data repositories
│   │   ├── runner/             # CommandLineRunners (DB test + data seed)
│   │   ├── security/           # JWT filter, handlers, SecurityUser, config
│   │   └── service/            # Business logic services
│   └── resources/
│       ├── application.yaml        # Default (H2/dev) configuration
│       ├── application-docker.yml  # Docker/MySQL configuration
│       ├── init.sql                # MySQL schema init
│       ├── static/css/             # Dashboard stylesheet
│       ├── static/js/              # Dashboard JavaScript
│       └── templates/              # Thymeleaf HTML pages
├── Dockerfile
└── docker-compose.yml
```

---

## Prerequisites

| Tool | Minimum Version | Purpose |
|---|---|---|
| Java | 17 | Run the application |
| Maven | 3.9 | Build the application |
| Docker | 24 | Container deployment |
| Docker Compose | 2.x | Multi-container orchestration |

> **Note:** Maven Wrapper (`./mvnw`) is included. You do not need Maven installed globally.

---

## Getting Started — Local (H2)

This profile uses an **in-memory H2 database**. All data is reset every time the application restarts.

### 1. Clone the repository

```bash
git clone https://github.com/your-org/crm-ticketing.git
cd crm-ticketing
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

Or build first then run the JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/*.jar
```

### 3. Verify startup

Open your browser and go to:

```
http://localhost:8080
```

You should see the landing page. The application automatically seeds roles, permissions, and an admin user on first startup. Check the console logs for:

```
✅ Admin user created successfully!
   Username: admin
   Password: Admin@123
```

---

## Getting Started — Docker (MySQL)

This profile runs the application and a MySQL 8.0 database inside Docker containers.

### 1. Build and start all services

```bash
docker-compose up -d --build
```

This will:
- Pull the MySQL 8.0 image
- Build the Spring Boot application image
- Start both containers on the `Crm_network` bridge network
- Wait for MySQL to be healthy before starting the application

### 2. Check container status

```bash
docker-compose ps
```

Both `Crm_mysql` and `Crm_app` should show status `running`.

### 3. Follow logs

```bash
# All services
docker-compose logs -f

# Application only
docker-compose logs -f app

# MySQL only
docker-compose logs -f mysql
```

### 4. Stop all services

```bash
docker-compose down
```

To also remove the persistent MySQL volume:

```bash
docker-compose down -v
```

### 5. Rebuild after code changes

```bash
docker-compose up -d --build
```

---

## Default Credentials

These are seeded automatically on every fresh startup.

### Application Login

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `Admin@123` |
| Email | `admin@example.com` |
| Role | `ROLE_SUPER_ADMIN` |

### H2 Database Console (dev profile only)

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;MODE=MySQL` |
| Username | `sa` |
| Password | *(leave blank)* |

### MySQL (Docker profile)

| Field | Value |
|---|---|
| Host | `localhost:3306` |
| Database | `Crm_db` |
| Username | `root` |
| Password | `root123` |

---

## API Overview

All REST endpoints require a `Bearer` token in the `Authorization` header unless marked as public.

```
Authorization: Bearer <your_access_token>
```

### Base URLs

| Domain | Base Path |
|---|---|
| Authentication | `/api/v1/auth` |
| Users | `/api/v1/users` |
| Roles | `/api/v1/roles` |
| Permissions | `/api/v1/permissions` |
| Profile | `/api/v1/profile` |
| Tickets | `/api/v1/tickets` |
| Ticket History | `/api/v1/ticketHistories` |
| Customers | `/api/customers` |
| Customer Requests | `/api/customer-requests` |
| Departments | `/api/departments` |
| Support Agents | `/api/agents` |
| SLA | `/api/slas` |
| Messages | `/api/messages` |
| Attachments | `/api/attachments` |

### Authentication Endpoints (Public)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Login and receive JWT tokens |
| `POST` | `/api/v1/auth/refresh` | Get a new access token using a refresh token |
| `POST` | `/api/v1/auth/logout` | Clear the security context |

### User Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/users/me` | Any authenticated | Get current user profile |
| `GET` | `/api/v1/users` | ADMIN | List all users |
| `GET` | `/api/v1/users/{id}` | ADMIN or owner | Get user by ID |
| `GET` | `/api/v1/users/username/{username}` | ADMIN | Get user by username |
| `PUT` | `/api/v1/users/{id}` | ADMIN or owner | Update user details |
| `POST` | `/api/v1/users/{id}/change-password` | ADMIN or owner | Change password |
| `POST` | `/api/v1/users/{id}/enable` | ADMIN | Enable a user account |
| `POST` | `/api/v1/users/{id}/disable` | ADMIN | Disable a user account |
| `POST` | `/api/v1/users/{id}/unlock` | ADMIN | Unlock a locked account |
| `POST` | `/api/v1/users/{id}/roles/{roleId}` | ADMIN | Assign a role |
| `DELETE` | `/api/v1/users/{id}/roles/{roleId}` | ADMIN | Remove a role |
| `DELETE` | `/api/v1/users/{id}` | ADMIN | Soft-delete a user |

### Ticket Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/tickets` | Create a ticket |
| `PUT` | `/api/v1/tickets/{id}` | Update a ticket |
| `DELETE` | `/api/v1/tickets/{id}` | Delete a ticket |
| `GET` | `/api/v1/tickets` | List all tickets |
| `GET` | `/api/v1/tickets/{id}` | Get ticket by ID |
| `GET` | `/api/v1/tickets/{id}/details` | Get ticket with all relations |
| `GET` | `/api/v1/tickets/{id}/attachments` | Get ticket with attachments |
| `GET` | `/api/v1/tickets/{id}/messages` | Get ticket with messages |
| `GET` | `/api/v1/tickets/{id}/ticketWithHistories` | Get ticket with change history |
| `GET` | `/api/v1/tickets/search/by-title` | Find ticket by title |
| `GET` | `/api/v1/tickets/search/by-priority` | Find ticket by priority |
| `GET` | `/api/v1/tickets/search/by-status` | Find ticket by status |
| `GET` | `/api/v1/tickets/with-department-agent` | All tickets with department and agent |
| `GET` | `/api/v1/tickets/by-department/{depId}/sla` | Tickets by department with SLA |

> All other domain endpoints (Customers, Departments, Agents, SLA, Messages, Attachments) follow the same CRUD + search pattern. See Swagger UI for the full list.

---

## Authentication Flow

### Step 1 — Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "Admin@123"
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600000,
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "roles": ["ROLE_SUPER_ADMIN", "ROLE_USER"]
    }
  }
}
```

### Step 2 — Use the token

```bash
curl http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Step 3 — Refresh the token

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
  }'
```

### Token Expiry

| Token | Expiry |
|---|---|
| Access token | 1 hour (3 600 000 ms) |
| Refresh token | 24 hours (86 400 000 ms) |

---

## Roles and Permissions

Roles and permissions are seeded automatically at startup. The system supports two layers of access control that can be combined.

### Roles

| Role | Priority | Description |
|---|---|---|
| `ROLE_SUPER_ADMIN` | 1 | Full access to all permissions |
| `ROLE_ADMIN` | 2 | User/role management and assignment |
| `ROLE_USER` | 10 | Read-only access |
| `ROLE_GUEST` | 20 | No permissions |

### Permissions

| Category | Permission | Description |
|---|---|---|
| USER | `CREATE_USER` | Create new users |
| USER | `READ_USER` | Read user details |
| USER | `UPDATE_USER` | Update user details |
| USER | `DELETE_USER` | Delete users |
| ROLE | `CREATE_ROLE` | Create new roles |
| ROLE | `READ_ROLE` | Read role details |
| ROLE | `UPDATE_ROLE` | Update role details |
| ROLE | `DELETE_ROLE` | Delete roles |
| ROLE | `ASSIGN_ROLE` | Assign roles to users |
| ADMIN | `VIEW_AUDIT_LOG` | View audit logs |
| ADMIN | `SYSTEM_CONFIG` | Configure system settings |
| ADMIN | `VIEW_PROFILE` | View user profiles |
| ADMIN | `MANAGE_PERMISSIONS` | Manage permissions |

### Access Control Examples

```java
// Role-based
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")

// Permission-based
@PreAuthorize("hasPermission('READ_USER')")

// Ownership check (user can access their own resource)
@PreAuthorize("hasRole('ADMIN') or @securityUserService.isCurrentUser(#id, authentication)")
```

---

## Swagger UI

Interactive API documentation is available when the application is running.

```
http://localhost:8080/swagger-ui.html
```

To test protected endpoints inside Swagger:

1. Call `POST /api/v1/auth/login` with the default credentials
2. Copy the `accessToken` from the response
3. Click the **Authorize** button (lock icon) at the top right
4. Enter `Bearer <your_token>` and click **Authorize**
5. All subsequent requests will include the token automatically

The raw OpenAPI spec (JSON) is available at:

```
http://localhost:8080/v3/api-docs
```

---

## H2 Console

Available in the **dev profile only** (not in Docker/MySQL mode).

```
http://localhost:8080/h2-console
```

| Field | Value |
|---|---|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;MODE=MySQL` |
| User Name | `sa` |
| Password | *(leave blank)* |

> All tables are created automatically by Hibernate (`ddl-auto: create-drop`). Data is lost on restart.

---

## Actuator & Monitoring

Spring Actuator endpoints are available for health checks and metrics.

| Endpoint | URL | Access |
|---|---|---|
| Health | `http://localhost:8080/actuator/health` | Public |
| Info | `http://localhost:8080/actuator/info` | Public |
| Metrics | `http://localhost:8080/actuator/metrics` | ADMIN |
| HTTP Exchanges | `http://localhost:8080/actuator/httpexchanges` | ADMIN |

Prometheus metrics are exported and can be scraped at:

```
http://localhost:8080/actuator/prometheus
```

Docker Compose health check polls `http://localhost:8080/actuator/health` every 30 seconds.

---

## Environment Variables

These can be set in `docker-compose.yml` or passed directly to the JVM.

| Variable | Default | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` | Active profile (`dev` or `docker`) |
| `SPRING_DATASOURCE_URL` | H2 URL | Full JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | `sa` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | *(blank)* | Database password |
| `JWT_SECRET` | Long default string | HS512 signing secret — **change in production** |
| `JAVA_OPTS` | `-Xmx512m -Xms256m` | JVM heap settings |
| `TZ` | `Asia/Tehran` | Container timezone |

> **Security:** Never commit a real `JWT_SECRET` to version control. Use a secret manager or environment injection in production.

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Run and skip tests (build only)
./mvnw clean package -DskipTests

# Run a specific test class
./mvnw test -Dtest=TicketServiceTest

# Run with coverage report (if JaCoCo is configured)
./mvnw verify
```

---

## Common Issues

### Application fails to start — port already in use

```bash
# Find the process using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>
```

### Docker: MySQL container not healthy

```bash
# Check MySQL logs
docker-compose logs mysql

# Force remove and recreate
docker-compose down -v
docker-compose up -d --build
```

### 401 Unauthorized on every request

- Confirm you are sending `Authorization: Bearer <token>` (not just the token)
- Check the token has not expired (access token lasts 1 hour)
- Use the refresh endpoint to get a new access token

### H2 console shows empty tables

The `dev` profile uses `ddl-auto: create-drop`. Data is seeded by `DataInitializer` at startup. If seeding failed, check the startup logs for stack traces from `DataInitializer`.

### Cannot connect to H2 console

Make sure you are using the **exact** JDBC URL:

```
jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;MODE=MySQL
```

Do **not** use `jdbc:h2:~/testdb` or any file-based URL.

### MapStruct-generated code missing after fresh clone

Run a full build to trigger annotation processing:

```bash
./mvnw clean compile
```

### Docker build fails — Maven cannot download dependencies

The project uses a custom Maven mirror (`mvnhub.ir`). If you are outside Iran or the mirror is unreachable, edit `.mvn/wrapper/maven-wrapper.properties` and remove the custom mirror URL, then rebuild.

---

## Web UI

A minimal Thymeleaf-based admin interface is included for quick user management without needing an API client.

| Page | URL | Description |
|---|---|---|
| Landing | `http://localhost:8080/` | Home page with login status |
| Login | `http://localhost:8080/login` | Username/password login form |
| Dashboard | `http://localhost:8080/dashboard` | User list with enable/disable controls |
| Tickets | `http://localhost:8080/ui/tickets` | Ticket list and management |

The UI communicates with the REST API using the JWT token stored in `localStorage`.

---

## License

MIT License — see `LICENSE` for details.
