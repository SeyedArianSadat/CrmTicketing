# 🚀 CRM Ticketing System

<p align="center">

Enterprise Customer Relationship Management & Ticketing System built with Spring Boot.

Java • Spring Boot • Spring Security • JPA/Hibernate • Thymeleaf • Bootstrap • MySQL/H2

</p>

---

# 📖 Overview

CRM Ticketing System is an enterprise-level web application designed to manage customers, support tickets, departments, inventory, attachments, permissions and user authentication in one centralized platform.

The application follows modern Spring Boot architecture and provides secure authentication, role-based authorization, clean layered architecture, validation, exception handling and scalable project organization.

---

# ✨ Features

- Authentication & Authorization
- Role Based Access Control (RBAC)
- Customer Management
- Support Ticket Management
- Departments
- Users
- Roles
- Permissions
- Inventory Module
- Inventory Transactions
- Product Management
- SLA Management
- Attachment Upload
- Dashboard
- Search
- Pagination
- Validation
- Soft Delete
- Optimistic Locking
- Audit Information
- Global Exception Handling
- Responsive UI

---

# 🛠 Tech Stack

Backend

- Java
- Spring Boot
- Spring Security
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven

Frontend

- Thymeleaf
- HTML5
- CSS3
- Bootstrap
- JavaScript

Database

- MySQL
- H2 Database

Tools

- IntelliJ IDEA
- Git
- GitHub
- Maven

---

# 🏗 Architecture

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database
```

Project follows Layered Architecture.

---

# 📁 Project Structure

```
src
├── main
│   ├── java
│   │    └── com
│   │         └── company
│   │              ├── controller
│   │              ├── service
│   │              ├── repository
│   │              ├── entity
│   │              ├── dto
│   │              ├── mapper
│   │              ├── security
│   │              ├── config
│   │              ├── exception
│   │              ├── validation
│   │              └── util
│   │
│   └── resources
│        ├── templates
│        ├── static
│        ├── application.yml
│        └── messages.properties
│
└── test
```

---

# 🔐 Security

- Spring Security
- BCrypt Password Encoder
- Login Authentication
- Authorization
- Role Based Access
- Session Management
- CSRF Protection

---

# 📦 Modules

### Authentication

- Login
- Logout

### Customer

- Create Customer
- Edit Customer
- Delete Customer
- Search Customer

### Ticket

- Create Ticket
- Assign Ticket
- Close Ticket
- Ticket History

### Inventory

- Products
- Inventory Items
- Transactions

### Administration

- Users
- Roles
- Permissions
- Departments

---

# 🗄 Database

Core Entities

- User
- Role
- Permission
- Customer
- Ticket
- Department
- Attachment
- SLA
- Inventory
- InventoryItem
- InventoryTransaction

---

# ⚙ Requirements

- Java 17+
- Maven 3.9+
- MySQL 8+
- IntelliJ IDEA

---

# 🚀 Installation

Clone project

```bash
git clone https://github.com/SeyedArianSadat/CrmTicketing.git
```

Go to project

```bash
cd CrmTicketing
```

Install dependencies

```bash
mvn clean install
```

Run application

```bash
mvn spring-boot:run
```

---

# ▶ Running

```
http://localhost:8080
```

---

# 📚 Build

```
mvn clean package
```

```
mvn test
```

```
mvn spring-boot:run
```

---

# 📂 Resources

```
src/main/resources

application.yml
templates/
static/
messages.properties
```

---

# 🔍 Validation

- Bean Validation
- Input Validation
- Custom Validation
- Exception Messages

---

# ⚠ Exception Handling

- Global Exception Handler
- Custom Exceptions
- Validation Exceptions
- Database Exceptions

---

# 📈 Future Improvements

- REST API
- JWT Authentication
- Docker Support
- Docker Compose
- Kafka Integration
- Redis Cache
- Elasticsearch
- Email Notification
- SMS Notification
- Grafana
- Prometheus
- CI/CD
- Kubernetes
- Microservices

---

# 🤝 Contributing

1. Fork Project

2. Create Feature Branch

```
git checkout -b feature/new-feature
```

3. Commit

```
git commit -m "Add feature"
```

4. Push

```
git push origin feature/new-feature
```

5. Open Pull Request

---

# 👨‍💻 Author

**Arian**

Java Backend Developer

GitHub

https://github.com/SeyedArianSadat

---

# 📄 License

This project is released under the MIT License.

---

# ⭐ If you like this project

Give it a ⭐ on GitHub.