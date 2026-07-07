Enterprise CRM Ticketing Platform

Built with clean architecture, enterprise standards and modern Spring ecosystem.

Developed by SeyedArianSadat

📖 Overview

CrmTicketing is a complete CRM Ticket Management platform developed using modern Spring technologies.

The project provides both:

Traditional MVC Web Application (Thymeleaf)
Fully documented REST API

The application demonstrates enterprise-level software architecture and follows clean coding principles such as:

Layered Architecture
DTO Pattern
Repository Pattern
Service Layer
JWT Authentication
Spring Security
Validation
Exception Handling
Docker Deployment
Swagger Documentation
Role & Permission Based Authorization

The system is designed to be easily extendable and production-ready.

✨ Key Features
Authentication
JWT Access Token
JWT Refresh Token
Stateless REST Authentication
Spring Security Integration
Login API
Logout Support
Password Encryption (BCrypt)
Authorization
Role Based Authorization

Examples:

ROLE_USER
ROLE_SUPER_ADMIN

Permission Based Authorization

Examples:

CREATE_USER
READ_USER
UPDATE_USER
DELETE_USER
CREATE_ROLE
UPDATE_ROLE
DELETE_ROLE
VIEW_PROFILE
SYSTEM_CONFIG

Supports:

URL Security
Method Security
Custom SecurityUser
Security Context
CRM Modules

The project contains complete CRUD operations for different CRM components.

Examples include:

Users
Roles
Permissions
Tickets
Ticket Histories
Customers
Support Agents
Attachments

Each module contains:

MVC Controller
REST Controller
Service
Repository
DTO
Mapper
Validation
Exception Handling


# 📂 Project Structure

CrmTicketing
│
├── src
│   ├── main
│   │
│   ├── java
│   │   └── com
│   │       └── company
│   │           └── crmticketing
│   │
│   │               ├── config
│   │               ├── controller
│   │               │   ├── mvc
│   │               │   └── rest
│   │               │
│   │               ├── dto
│   │               │   ├── request
│   │               │   ├── response
│   │               │   ├── user
│   │               │   ├── role
│   │               │   ├── permission
│   │               │   ├── customer
│   │               │   ├── supportagent
│   │               │   ├── ticket
│   │               │   ├── tickethistory
│   │               │   └── attachment
│   │               │
│   │               ├── exception
│   │               ├── mapper
│   │               ├── model
│   │               ├── repository
│   │               ├── security
│   │               │   ├── config
│   │               │   ├── filter
│   │               │   ├── handler
│   │               │   ├── model
│   │               │   ├── provider
│   │               │   └── service
│   │               │
│   │               ├── service
│   │               └── CrmTicketingApplication.java
│   │
│   └── resources
│       ├── static
│       ├── templates
│       ├── application.yml
│       └── init.sql
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md


🏛 Architecture

The project follows a layered enterprise architecture to ensure maintainability, scalability and clean separation of concerns.

                Client

        Browser / Swagger UI

                │

      Spring Security Filters

                │

      REST Controller / MVC Controller

                │

           Service Layer

                │

           MapStruct Mapper

                │

       Spring Data Repository

                │

        Hibernate / JPA

                │

      H2 Database / MySQL


🧩 Project Characteristics

✔ Enterprise Structure

✔ DTO Based Design

✔ JWT Authentication

✔ Spring Security

✔ MVC + REST

✔ Thymeleaf Dashboard

✔ Swagger UI

✔ Docker Support

✔ Docker Compose

✔ H2 Database

✔ MySQL Ready

✔ Validation

✔ Global Exception Handling

✔ Logging

✔ Actuator Monitoring

✔ MapStruct Mapping

✔ Lombok

✔ Maven Build

✔ Layered Architecture

✔ Clean Code Principles

🎯 Main Goals

This project was created to demonstrate how a modern Spring Boot application should be structured using enterprise development standards.

The focus is on:

Security
Maintainability
Scalability
Readability
Separation of Concerns
Best Practices
Production Readiness
📌 Highlights
Spring Boot 4
Java 17
Spring Security 6
JWT Authentication
RESTfull APIs
Thymeleaf MVC
OpenAPI / Swagger
Docker Deployment
MySQL & H2 Support
Actuator Monitoring
Validation
Logging
Clean Architecture
Enterprise Coding Standards
Fully Layered Design
DTO Pattern
Repository Pattern
Service Pattern
Production Ready Structure

⚡ Technology Stack

| Category         | Technology         |
| ---------------- | ------------------ |
| Language         | Java 17            |
| Framework        | Spring Boot 4      |
| Security         | Spring Security 6  |
| Authentication   | JWT                |
| ORM              | Hibernate          |
| Persistence      | Spring Data JPA    |
| Mapping          | MapStruct          |
| Validation       | Jakarta Validation |
| Build Tool       | Maven              |
| Database         | H2 / MySQL         |
| Template Engine  | Thymeleaf          |
| Documentation    | Swagger OpenAPI    |
| Monitoring       | Spring Actuator    |
| Metrics          | Prometheus         |
| Containerization | Docker             |
| IDE              | IntelliJ IDEA      |

🔐 Security

The application implements enterprise-level security using Spring Security and JWT.
Authentication
Form Login (MVC)
JWT Authentication (REST API)
BCrypt Password Encryption
Access Token
Refresh Token
Authorization

Role Based

ROLE_USER
ROLE_SUPER_ADMIN

Permission Based

CREATE_USER
READ_USER
UPDATE_USER
DELETE_USER
CREATE_ROLE
UPDATE_ROLE
DELETE_ROLE
ASSIGN_ROLE
MANAGE_PERMISSIONS
VIEW_PROFILE
VIEW_AUDIT_LOG
SYSTEM_CONFIG
Security Components
SecurityConfig
JwtAuthenticationFilter
JwtTokenProvider
SecurityUser
CustomUserDetailsService
AuthenticationEntryPoint
AccessDeniedHandler
LogoutHandler
🌐 REST API

Every module exposes REST endpoints.

Examples

Module	Endpoint
Authentication	/api/v1/auth
Users	/api/v1/users
Roles	/api/v1/roles
Permissions	/api/v1/permissions
Customers	/api/v1/customers
Support Agents	/api/v1/supportAgents
Tickets	/api/v1/tickets
Ticket Histories	/api/v1/ticketHistories
Attachments	/api/v1/attachments

All endpoints are documented using Swagger/OpenAPI.

🖥 MVC

The project also includes a complete server-side rendered web application using Thymeleaf.

Features include

Login Page
Dashboard
CRUD Pages
Validation
Error Pages
Session Authentication
📚 Swagger

Interactive API documentation is available.

http://localhost:8080/swagger-ui.html

OpenAPI JSON

http://localhost:8080/v3/api-docs
❤️ Actuator

Production monitoring endpoints

/actuator/health

/actuator/info

/actuator/metrics

/actuator/httpexchanges
🐳 Docker

The application can run entirely inside Docker.

Containers

Spring Boot
MySQL 8

Features

Health Checks
Persistent Volumes
Dedicated Network
Automatic Startup
📊 Monitoring

Integrated support for

Spring Boot Actuator
Prometheus Metrics
Health Monitoring
Application Metrics
✅ Validation

Uses Jakarta Bean Validation.

Examples

@NotBlank
@Email
@Size
@NotNull
@Positive
📝 Logging

Uses SLF4J + Logback.

Logging is implemented across

Controllers
Services
Security
JWT Authentication
Exception Handling
🗂 Mapping

DTO mapping is handled using MapStruct.

Advantages

Cleaner Code
Better Performance
Compile-Time Safety
📦 Build

Build the project

mvn clean install



Seyed Arian Sadat

GitHub

https://github.com/SeyedArianSadat

📄 License

This project is released under the MIT License.

Feel free to use, modify and distribute this project.

⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub.

It helps the project grow and motivates future development.


Thanks for visiting ❤️

Made with Spring Boot • Java • Docker • JWT • Spring Security


