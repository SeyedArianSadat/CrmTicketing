<div align="center">

# 🚀 CRM Ticketing System

### Enterprise Customer Relationship Management & Ticketing Platform

A modern, secure, scalable CRM & Help Desk platform built with Spring Boot, Spring Security, JWT Authentication, REST APIs, Docker, Prometheus and Grafana.

---

![Java](https://img.shields.io/badge/Java-17-red?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-success?style=for-the-badge)
![JWT](https://img.shields.io/badge/JWT-Authentication-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-orange?style=for-the-badge)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-brightgreen?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)
![Prometheus](https://img.shields.io/badge/Prometheus-Monitoring-E6522C?style=for-the-badge)
![Grafana](https://img.shields.io/badge/Grafana-Dashboard-F46800?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-yellow?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?style=for-the-badge)

</div>

---

# 📖 Table of Contents

- Overview
- Features
- Technology Stack
- Architecture
- Project Structure
- Modules
- Database
- Authentication
- Security
- REST API
- Swagger Documentation
- Monitoring
- Docker
- Installation
- Configuration
- Running the Project
- Build
- Testing
- Folder Structure
- Logging
- Validation
- Exception Handling
- Screenshots
- Future Improvements
- Deployment
- Contributing
- License
- Author

---

# 📌 Overview

CRM Ticketing System is an enterprise-level Customer Relationship Management platform designed to simplify customer support operations, ticket management, inventory management and user administration.

The application follows a clean layered architecture using Spring Boot and provides secure authentication using JWT, role-based authorization with Spring Security, interactive API documentation with Swagger/OpenAPI, containerized deployment using Docker, and production-ready monitoring through Prometheus and Grafana.

The project is structured according to enterprise software development standards and demonstrates best practices in backend development including validation, exception handling, DTO mapping, repository pattern, layered architecture and secure REST API development.

---

# 🎯 Main Goals

- Build a secure CRM platform
- Manage customers efficiently
- Handle support tickets
- Track departments
- Manage inventory
- Secure APIs using JWT
- Monitor application health
- Provide production-ready deployment
- Follow enterprise architecture
- Demonstrate modern Spring Boot development

---

# ✨ Features

## Authentication

- JWT Authentication
- Secure Login
- Logout
- Password Encryption (BCrypt)
- Token Validation
- Stateless Authentication

---

## Authorization

- Spring Security
- Role Based Access Control
- Permission Management
- Protected REST Endpoints

---

## Customer Management

- Create Customer
- Update Customer
- Delete Customer
- Search Customer
- Customer History

---

## Ticket Management

- Create Ticket
- Assign Ticket
- Update Ticket
- Close Ticket
- Ticket Priority
- Ticket Status
- Ticket History

---

## Department Management

- Create Department
- Update Department
- Delete Department
- Department Assignment

---

## User Management

- Create User
- Update User
- Delete User
- User Roles
- User Permissions

--

## Attachment Module

- Upload Files
- Download Files
- Store Attachments
- Ticket Attachments

---
 And other classes like this ....
---

## Monitoring

- Spring Boot Actuator
- Prometheus Metrics
- Grafana Dashboard
- JVM Monitoring
- HTTP Metrics
- Memory Usage
- CPU Usage

---

## API

- RESTful APIs
- JSON Responses
- Validation
- Exception Handling
- Swagger UI
- OpenAPI Specification

---

## Database

- Spring Data JPA
- Hibernate ORM
- MySQL
- H2 Database
- Relationships
- Transactions

---

## Docker

- Docker Support
- Docker Compose
- Containerized Deployment
- Easy Environment Setup

---

## Logging

- Structured Logging
- Request Logging
- Error Logging
- Application Logs

---

## Validation

- Bean Validation
- DTO Validation
- Request Validation
- Custom Validation

---

## Exception Handling

- Global Exception Handler
- Custom Exceptions
- Validation Errors
- API Error Responses

---

## Developer Friendly

- Layered Architecture
- DTO Pattern
- Repository Pattern
- Service Layer
- Clean Code
- SOLID Principles
- Scalable Structure

---
# 🏗 System Architecture

The project follows a layered architecture that separates business logic from presentation and persistence layers, making the application easier to maintain, test and scale.

```
                    Client
                       │
        ┌──────────────┴──────────────┐
        │                             │
   Thymeleaf UI                  REST API
        │                             │
        └──────────────┬──────────────┘
                       │
              Spring Security
                 JWT Filter
                       │
                 Controllers
                       │
                  Service Layer
                       │
                Repository Layer
                       │
                 Hibernate / JPA
                       │
                  MySQL / H2
```

---

# 📦 Technology Stack

## Backend

- Java 17
- Spring Boot
- Spring Security
- Spring MVC
- Spring Data JPA
- Hibernate ORM
- Maven
- Lombok
- MapStruct

---

## Frontend

- Thymeleaf
- HTML5
- CSS3
- Bootstrap 5
- JavaScript

---

## Database

- MySQL
- H2 Database

---

## API

- REST API
- OpenAPI 3
- Swagger UI

---

## Security

- JWT Authentication
- BCrypt Password Encoder
- Spring Security

---

## Monitoring

- Spring Boot Actuator
- Prometheus
- Grafana

---

## DevOps

- Docker
- Docker Compose
- Git
- GitHub

---

# 📁 Project Structure

```
crm-ticketing
│
├── src
│   ├── main
│   │
│   ├── java
│   │   └── com
│   │       └── arian
│   │           │
│   │           ├── config
│   │           ├── controller
│   │           ├── dto
│   │           ├── entity
│   │           ├── exception
│   │           ├── mapper
│   │           ├── repository
│   │           ├── security
│   │           ├── service
│   │           ├── util
│   │           ├── validation
│   │           └── CrmApplication.java
│   │
│   └── resources
│       ├── static
│       ├── templates
│       ├── application.yml
│       └── logback.xml
│
├── docker
│
├── docker-compose.yml
│
├── Dockerfile
│
├── pom.xml
│
└── README.md
```

---

# 📂 Package Description

| Package | Description |
|----------|-------------|
| config | Spring Configuration |
| controller | MVC & REST Controllers |
| dto | Request / Response DTOs |
| entity | JPA Entities |
| repository | Spring Data Repositories |
| service | Business Logic |
| mapper | MapStruct Mappers |
| security | JWT & Spring Security |
| validation | Custom Validators |
| exception | Global Exception Handling |
| util | Utility Classes |

---

# 🔐 Authentication Flow

```
User Login
     │
     ▼
AuthenticationManager
     │
     ▼
Verify Username & Password
     │
     ▼
Generate JWT Token
     │
     ▼
Return Token
     │
     ▼
Client Stores JWT
     │
     ▼
Authorization Header
     │
     ▼
JWT Filter
     │
     ▼
Protected Endpoint
```

---

# 🛡 Security Features

- JWT Authentication
- Stateless Session
- Spring Security Filter Chain
- BCrypt Password Encryption
- Authentication Manager
- Authorization Manager
- Role-Based Access Control (RBAC)
- Endpoint Protection
- Access Control
- Custom Authentication Entry Point

---

# 🌐 REST API

The application exposes RESTful APIs for all core business modules.

## Available APIs

- Authentication API
- Customer API
- Customer request
- Message API
- TicketHistory API
- Sla Api
- Ticket API
- Department API
- User API
- SupportAgent Api
- Role API
- Permission API
- Attachment API

---

# 📖 Swagger Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

Swagger provides:

- Interactive API Testing
- Request Examples
- Response Examples
- Authentication Support
- Schema Documentation

---

# 📊 Monitoring

Monitoring is implemented using Spring Boot Actuator, Prometheus and Grafana.

### Actuator

```
http://localhost:8080/actuator
```

### Health

```
/actuator/health
```

### Metrics

```
/actuator/metrics
```

### Prometheus

```
http://localhost:9090
```

### Grafana

```
http://localhost:3000
```

Available Dashboards

- JVM Metrics
- Heap Memory
- CPU Usage
- HTTP Requests
- Response Time
- Active Sessions
- Garbage Collection
- Thread Statistics

---
# 🐳 Docker Support

The application is fully containerized and can be deployed using Docker and Docker Compose.

## Docker Components

- Spring Boot Application
- MySQL Database
- Prometheus
- Grafana

---

# 🐋 Docker Architecture

```
                    Docker Compose
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
   Spring Boot          MySQL          Prometheus
        │                                   │
        └───────────────────────────────────┘
                        │
                        ▼
                    Grafana
```

---

# 📦 Docker Containers

| Container | Description |
|------------|-------------|
| crm-app | Spring Boot Application |
| mysql | MySQL Database |
| prometheus | Metrics Collection |
| grafana | Monitoring Dashboard |

---

# ⚙️ Prerequisites

Before running the project make sure the following software is installed.

- Java 17+
- Maven 3.9+
- Docker
- Docker Compose
- Git

Verify installation

```bash
java -version

mvn -version

docker --version

docker compose version
```

---

# 🚀 Installation

Clone Repository

```bash
git clone https://github.com/SeyedArianSadat/CrmTicketing.git
```

Move into project

```bash
cd CrmTicketing
```

---

# 📥 Install Dependencies

```bash
mvn clean install
```

---

# 🐳 Run with Docker

Build Containers

```bash
docker compose build
```

Start Containers

```bash
docker compose up -d
```

View Running Containers

```bash
docker ps
```

View Logs

```bash
docker compose logs -f
```

Stop Containers

```bash
docker compose down
```

Remove Volumes

```bash
docker compose down -v
```

---

# ▶ Run Without Docker

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/crm-ticketing.jar
```

---

# ⚙ Configuration

Application configuration is stored inside

```
src/main/resources/application.yml
```

Important sections include

- Server Configuration
- Database
- JWT
- Swagger
- Logging
- Actuator
- Prometheus
- JPA
- Hibernate

---

# 🔑 Environment Variables

Example

```properties
SPRING_DATASOURCE_URL=

SPRING_DATASOURCE_USERNAME=

SPRING_DATASOURCE_PASSWORD=

JWT_SECRET=

JWT_EXPIRATION=

SERVER_PORT=
```

---

# 🌍 Default URLs

Application

```
http://localhost:8080
```

Swagger

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI

```
http://localhost:8080/v3/api-docs
```

Actuator

```
http://localhost:8080/actuator
```

Health

```
http://localhost:8080/actuator/health
```

Metrics

```
http://localhost:8080/actuator/metrics
```

Prometheus

```
http://localhost:9090
```

Grafana

```
http://localhost:3000
```

---

# 🏗 Build

Compile

```bash
mvn clean compile
```

Package

```bash
mvn clean package
```

Install

```bash
mvn clean install
```

Skip Tests

```bash
mvn clean package -DskipTests
```

---

# 📑 Logging

Logging supports

- INFO
- DEBUG
- WARN
- ERROR
- Request Logging
- Exception Logging
- SQL Logging

Log file example

```
logs/application.log
```

---

# ✅ Validation

Validation is implemented using Jakarta Bean Validation.

Supported annotations include

- @NotNull
- @NotBlank
- @Email
- @Size
- @Pattern

---

# ⚠ Exception Handling

Global Exception Handling covers

- Resource Not Found
- Validation Errors
- Authentication Errors
- Authorization Errors
- Database Errors
- Internal Server Errors

Every API returns a consistent JSON error response.

---

# 🔍 Monitoring

Metrics available

- JVM Memory
- Heap Usage
- CPU Usage
- Thread Count
- Garbage Collection
- HTTP Requests
- Request Duration
- Active Sessions
- Database Connections
- Application Health

---

# 🚀 Performance

Project is optimized using

- Spring Boot Auto Configuration
- Hikari Connection Pool
- Hibernate Optimization
- DTO Mapping
- Lazy Loading
- Pagination
- Index-friendly Queries

---
# 🚀 Deployment

The application can be deployed in different environments.

## Local Development

- Java 17
- Maven
- MySQL
- H2

## Docker

```bash
docker compose up -d
```

## Production

The application is production-ready and supports:

- Docker Deployment
- Reverse Proxy (Nginx)
- MySQL
- Prometheus Monitoring
- Grafana Dashboards

---

# 📈 Future Improvements

- Refresh Token Support
- Email Notifications
- Kafka Event Streaming
- Redis Cache
- Elasticsearch
- Kubernetes Deployment
- CI/CD Pipeline (GitHub Actions)
- Multi-language Support
- File Storage (AWS S3 / MinIO)
- Audit Logs
- Rate Limiting

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository

2. Create a new branch

```bash
git checkout -b feature/new-feature
```

3. Commit changes

```bash
git commit -m "Add new feature"
```

4. Push

```bash
git push origin feature/new-feature
```

5. Open a Pull Request

---

# 📄 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Arian**

Java Backend Developer

GitHub

https://github.com/SeyedArianSadat

---

# ⭐ Support

If you found this project useful, please consider giving it a ⭐ on GitHub.

Your support helps improve the project and encourages future development.