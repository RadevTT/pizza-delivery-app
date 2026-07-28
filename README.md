# 🍕 Pizza Delivery Application

A full-stack Spring Boot web application for managing pizzas, customer orders and deliveries with a dedicated Delivery microservice.

The project was developed as an individual assignment for the **SoftUni Spring Advanced** course and demonstrates a modern Spring Boot architecture using layered design, authentication, authorization, microservices, Spring Events, AOP logging, scheduling, caching, resilience patterns, PDF export and automated testing.

---

# Architecture

The system consists of two Spring Boot applications:

- **Pizza Delivery Application** – the main web application
- **Delivery Service** – a separate microservice responsible for delivery management

The applications communicate through REST APIs using **Spring Cloud OpenFeign**.

To improve reliability, the project uses **Spring Cloud Circuit Breaker (Resilience4j)** with fallback support when the Delivery Service is unavailable.

---

# Features

## Authentication & Authorization

- User registration
- User login
- User logout
- BCrypt password encryption
- Spring Security integration
- Role-based authorization
- USER and ADMIN roles

---

## Pizza Management (ADMIN)

- Add pizza
- Edit pizza
- Delete pizza
- Browse pizza menu
- Pizza image support
- Cached pizza catalog using Spring Cache
- Export pizza menu as PDF

---

## Orders

- Create order
- View personal orders
- View order details
- Automatic total price calculation
- Track order status

### Order workflow

- PENDING
- PREPARING
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED

---

## Delivery Microservice

The application communicates with a separate Delivery Service using Spring Cloud OpenFeign.

Implemented functionality:

- Automatically create delivery after order creation
- Synchronize delivery status with order status
- Dispatch deliveries
- Complete deliveries
- Cancel deliveries

---

## Spring Events

The application uses Spring Events to decouple business logic.

Implemented event:

- OrderCreatedEvent
- OrderCreatedEventListener

---

## Scheduled Tasks

Automatic background processing using Spring Scheduler.

Implemented jobs:

- Cancel expired pending orders
- Dispatch delayed preparing orders

---

## User Profile

- View profile
- Edit email
- Change password
- Display assigned roles

---

## Administration

Administrators can:

- View all orders
- View order details
- Change order status
- Cancel orders
- Manage pizzas
- Manage users
- Grant ADMIN role
- Remove ADMIN role

---

## PDF Export

Administrators can export the complete pizza menu as a PDF document.

The generated report contains:

- Pizza name
- Description
- Price

---

## Error Handling

Custom exceptions:

- UsernameAlreadyExistsException
- UserNotFoundException
- PizzaNotFoundException
- OrderNotFoundException
- RoleNotFoundException
- DeliveryServiceUnavailableException

Global exception handling is implemented using Spring MVC.

---

## Logging

The project uses **SLF4J** together with **Spring AOP**.

Logged events include:

- User registration
- Profile updates
- Pizza management
- Order creation
- Order cancellation
- Order status changes
- Delivery status changes

Additionally, every service method execution is automatically logged through a custom AOP aspect, including execution time and exceptions.

---

## Caching

Spring Cache is used for:

- Pizza list
- Pizza lookup by ID

The cache is automatically invalidated after adding, editing or deleting pizzas.

---

## Resilience

The project uses **Spring Cloud Circuit Breaker (Resilience4j)**.

Fallback support is implemented for Delivery Service communication.

If the Delivery Service becomes unavailable, the application continues working gracefully.

---

# Technologies

- Java 17
- Spring Boot 3.4.0
- Spring Security
- Spring Data JPA
- Hibernate
- Spring Cache
- Spring Scheduler
- Spring Events
- Spring AOP
- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker (Resilience4j)
- Thymeleaf
- MySQL
- OpenPDF
- Maven
- Bootstrap 5
- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- JaCoCo

---

# Database

The application uses **MySQL**.

Domain entities use UUID identifiers.

Entity relationships:

- User ↔ Role
- User ↔ Order
- Order ↔ OrderItem
- OrderItem ↔ Pizza

Passwords are stored using BCrypt hashing.

---

# Security

## Guest

- Register
- Login

## USER

- Browse pizzas
- Create orders
- View personal orders
- View order details
- View and edit profile

## ADMIN

All USER permissions plus:

- Add pizzas
- Edit pizzas
- Delete pizzas
- Export pizza menu as PDF
- View all orders
- Change order status
- Cancel orders
- Manage users
- Grant administrator role
- Remove administrator role

Access restrictions are enforced using Spring Security.

Unauthorized users are redirected to a custom **Access Denied** page.

---

# Testing

The project includes automated unit and controller tests built with:

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- JaCoCo

Covered components:

### Main Application

- PizzaService
- UserService
- OrderService
- OrderController
- PizzaController
- ProfileController
- PdfController
- PdfExportService
- LoggingAspect
- OrderCreatedEvent

### Delivery Service

- DeliveryService
- DeliveryController
- Delivery integration tests

Project statistics:

- ✅ 54 automated tests
- ✅ JaCoCo code coverage reports
- ✅ 77% line coverage (main application)
- ✅ 98% line coverage (delivery-service)

---
# Project Structure

```text
pizza-delivery-application
├── src
│   ├── aspect
│   ├── client
│   ├── client.dto
│   ├── client.fallback
│   ├── config
│   ├── controller
│   ├── event
│   ├── exception
│   ├── init
│   ├── model
│   │   ├── dto
│   │   ├── entity
│   │   └── enums
│   ├── repository
│   ├── scheduler
│   ├── service
│   ├── service.impl
│   ├── templates
│   └── test
│
└── delivery-service
    ├── controller
    ├── exception
    ├── model
    ├── repository
    ├── service
    ├── service.impl
    └── test
```

---

# Setup

## 1. Create databases

```sql
CREATE DATABASE pizza_delivery;
CREATE DATABASE pizza_delivery_service;
```

---

## 2. Configure the databases

Edit:

```text
src/main/resources/application.properties

delivery-service/src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizza_delivery
spring.datasource.username=root
spring.datasource.password=your_password
```

Configure the Delivery Service in the same way.

---

## 3. Start the Delivery Service

Run:

```text
DeliveryServiceApplication
```

Default port:

```text
8081
```

---

## 4. Start the Main Application

Run:

```text
PizzaDeliveryApplication
```

Default port:

```text
8080
```

---

## 5. Generate the Pizza Menu PDF

Log in as an administrator.

Open:

```text
/pizzas
```

Click:

```text
Export PDF
```

A PDF document containing the complete pizza menu will be generated and downloaded automatically.

---

## 6. Run Tests

```bash
./mvnw test
```

Generate JaCoCo reports:

```bash
./mvnw verify
```

Reports are generated in:

```text
target/site/jacoco/index.html

delivery-service/target/site/jacoco/index.html
```

---

# Author

**Teodor Radev**

Spring Advanced Individual Project

SoftUni – June 2026

---

# License

This project was developed for educational purposes as part of the SoftUni Spring Advanced course.