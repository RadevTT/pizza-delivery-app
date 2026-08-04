# 🍕 Pizza Delivery Application

A full-stack Spring Boot web application for managing pizzas, customer orders and deliveries through a dedicated Delivery microservice.

The project was developed as an individual assignment for the **Spring Advanced** course at **SoftUni**. It demonstrates a modern Spring Boot architecture based on layered design, authentication and authorization with Spring Security, REST microservices, Spring Cloud OpenFeign, caching, scheduling, logging, event-driven programming, PDF export and automated testing.

The application consists of two independent Spring Boot applications communicating through REST APIs. The main application is responsible for user interaction and business logic, while the Delivery Service manages delivery-related operations.

---

# Architecture

The project consists of two independent Spring Boot applications.

## Main Application

Responsible for:

- User authentication
- Pizza management
- Customer orders
- User profiles
- Administration
- Communication with the Delivery Service
- PDF export
- Scheduling
- Caching

Default port:

```
8080
```

---

## Delivery Service

Responsible for:

- Creating deliveries
- Updating delivery status
- Returning delivery information
- Deleting deliveries when orders are cancelled

The Delivery Service exposes a REST API and is consumed through **Spring Cloud OpenFeign**.

Default port:

```
8081
```

---

## Communication

The applications communicate using REST APIs through **Spring Cloud OpenFeign**.

To improve reliability, the project implements **Spring Cloud Circuit Breaker (Resilience4j)** with fallback support when the Delivery Service is unavailable.

The Delivery Service is completely independent and can be started, stopped or deployed separately from the main application.

---

# Technology Stack

## Backend

- Java 17
- Spring Boot 3.4.0
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

## Cloud

- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker (Resilience4j)

## Additional Spring Modules

- Spring Cache
- Spring Scheduler
- Spring Events
- Spring AOP
- Spring Validation

## Frontend

- Thymeleaf
- Bootstrap 5

## Database

- MySQL
- UUID primary keys
- BCrypt password hashing

## Build Tools

- Maven

## Testing

- JUnit 5
- Mockito
- MockMvc
- Spring Boot Test
- JaCoCo

## Additional Libraries

- OpenPDF

---

# Main Features

The application provides complete functionality for managing pizzas, customer orders and deliveries.

Implemented features include:

- User registration
- User authentication
- User authorization
- Pizza management
- Order management
- Delivery management
- User profile management
- Administration panel
- Scheduled background jobs
- PDF export
- Spring Cache
- Spring Events
- AOP logging
- REST microservice communication
- Automated testing

---

# Authentication & Authorization

Authentication and authorization are implemented using **Spring Security**.

Supported functionality:

- User registration
- User login
- User logout
- BCrypt password encryption
- Role-based authorization
- Custom login page
- Access denied page

The application defines two roles:

## USER

Users can:

- Browse pizzas
- Create orders
- View their own orders
- View order details
- Edit their profile
- Change password

## ADMIN

Administrators have all USER permissions plus:

- Add pizzas
- Edit pizzas
- Delete pizzas
- View all users
- Grant ADMIN role
- Remove ADMIN role
- View all orders
- Change order status
- Export pizza menu as PDF

---

# Pizza Management

Pizza management is available only for administrators.

Implemented functionality:

- Add pizza
- Edit pizza
- Delete pizza
- Browse pizza catalog
- Display pizza images
- Display pizza descriptions
- Display pizza prices

To improve performance, the pizza catalog is cached using **Spring Cache**.

The cache is automatically refreshed whenever a pizza is added, edited or deleted.

---

# Order Management

Authenticated users can create pizza orders directly from the catalog.

Implemented functionality:

- Create order
- View personal orders
- View order details
- Automatic total price calculation
- Automatic delivery creation
- Order status tracking

Supported order statuses:

- PENDING
- PREPARING
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED

Whenever a new order is created, the application automatically communicates with the Delivery Service to create the corresponding delivery.

---

# Delivery Microservice

The Delivery Service is implemented as a separate Spring Boot application.

The communication between the two applications is performed using **Spring Cloud OpenFeign**.

Implemented operations include:

- Create delivery
- Retrieve delivery information
- Update delivery status
- Delete delivery

The delivery status is synchronized with the corresponding order status inside the main application.
---

# Spring Events

The application uses Spring's event-driven programming model to decouple business logic from secondary processing.

Implemented event:

- OrderCreatedEvent

Implemented listener:

- OrderCreatedEventListener

Whenever a new order is successfully created, an `OrderCreatedEvent` is published.

The listener receives the event and logs information about the newly created order without affecting the main order creation process.

Using Spring Events improves code separation and makes the application easier to maintain and extend.

---

# Scheduled Tasks

The project includes scheduled background jobs using **Spring Scheduler**.

Implemented scheduled tasks:

## Cancel Expired Orders

Orders that remain in **PENDING** state longer than the configured period are automatically cancelled.

## Dispatch Orders

Orders that stay in **PREPARING** state for too long are automatically moved to **OUT_FOR_DELIVERY**.

Using scheduled tasks automates repetitive business processes without user interaction.

---

# Caching

The application uses **Spring Cache** to improve performance.

Cached operations include:

- Retrieving the pizza catalog
- Retrieving a pizza by its identifier

The cache is automatically invalidated after:

- Adding a pizza
- Editing a pizza
- Deleting a pizza

This ensures that users always receive up-to-date information while reducing unnecessary database queries.

---

# AOP Logging

The project uses **Spring AOP** together with **SLF4J** for automatic service logging.

The logging aspect intercepts all service layer methods and records:

- Method execution start
- Method execution completion
- Execution time
- Exceptions

This approach removes logging code from the business logic and centralizes application logging in a single component.

---

# PDF Export

Administrators can export the complete pizza catalog as a PDF document.

The generated document contains:

- Pizza name
- Description
- Price

The PDF is generated dynamically using the **OpenPDF** library and is downloaded directly from the application.

Access to the export functionality is protected with Spring Security and is available only to users with the **ADMIN** role.

---

# Security

The application is secured using **Spring Security**.

Implemented security features include:

- Authentication
- Authorization
- BCrypt password hashing
- Session-based login
- Role-based access control
- Protected administrative endpoints
- Custom login page
- Custom Access Denied page

Administrative functionality is accessible only to users with the **ADMIN** role.

---

# Exception Handling

The project implements centralized exception handling.

Custom exceptions include:

- UsernameAlreadyExistsException
- UserNotFoundException
- PizzaNotFoundException
- OrderNotFoundException
- RoleNotFoundException
- DeliveryServiceUnavailableException

Exceptions are handled through a global exception handler, providing user-friendly error pages instead of raw server errors.

---

# Database

The application uses **MySQL** as the primary database.

Two separate databases are used:

## Main Application

```
pizza_delivery
```

Stores:

- Users
- Roles
- Pizzas
- Orders
- Order Items

## Delivery Service

```
pizza_delivery_service
```

Stores:

- Deliveries

The applications communicate only through REST APIs and do not share a database.

Primary keys are generated using UUID identifiers.

Passwords are stored using BCrypt hashing.

---

# Testing

The project contains automated tests for the most important application components.

Tested areas include:

- Services
- Controllers
- Security
- PDF Export
- Spring Events
- Spring AOP
- Delivery Service

Testing technologies:

- JUnit 5
- Mockito
- MockMvc
- Spring Boot Test
- JaCoCo

Project statistics:

- ✅ 66 automated tests
- ✅ 77% line coverage (main application)
- ✅ 98% line coverage (delivery-service)

Both applications generate JaCoCo HTML reports after running:

```bash
./mvnw verify
```

The reports are available at:

```text
target/site/jacoco/index.html

delivery-service/target/site/jacoco/index.html
```

---

# Advanced Features

In addition to the required functionality, the project also implements several advanced Spring Boot features:

- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker (Resilience4j)
- Spring Events
- Spring AOP
- Spring Scheduler
- Spring Cache
- PDF Export
- JaCoCo Code Coverage
- REST Microservice Communication

These features improve the application's architecture, maintainability, reliability and overall code quality.
---

# Project Structure

The project follows a layered architecture that separates responsibilities between controllers, services, repositories and persistence models.

```
pizza-delivery-application
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── bg.softuni.pizza_delivery_application
│   │   │
│   │   ├── aspect
│   │   ├── client
│   │   ├── config
│   │   ├── controller
│   │   ├── event
│   │   ├── exception
│   │   ├── init
│   │   ├── model
│   │   │   ├── dto
│   │   │   ├── entity
│   │   │   └── enums
│   │   ├── repository
│   │   ├── scheduler
│   │   ├── service
│   │   └── service.impl
│   │
│   └── resources
│       ├── static
│       ├── templates
│       └── application.properties
│
└── delivery-service
    ├── controller
    ├── service
    ├── repository
    ├── model
    └── application.properties
```

The project follows common Spring Boot design patterns including:

- Layered Architecture
- Repository Pattern
- Service Layer Pattern
- DTO Pattern
- Dependency Injection
- Event-Driven Architecture
- Circuit Breaker Pattern

---

# Setup

## Requirements

Before running the project make sure you have installed:

- Java 17
- Maven
- MySQL 8+
- Git

---

## Create the databases

Create two MySQL databases:

```sql
CREATE DATABASE pizza_delivery;
CREATE DATABASE pizza_delivery_service;
```

---

## Configure the applications

Update the `application.properties` file in both applications.

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizza_delivery
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Do the same for the Delivery Service using the `pizza_delivery_service` database.

---

## Running the applications

### 1. Start the Delivery Service

Run:

```
DeliveryServiceApplication
```

The service starts on:

```
http://localhost:8081
```

---

### 2. Start the Main Application

Run:

```
PizzaDeliveryApplication
```

The application starts on:

```
http://localhost:8080
```

---

## Using the application

After starting both applications:

- Register a new account or log in.
- Browse the pizza catalog.
- Create pizza orders.
- View your personal orders.
- Edit your profile.
- If logged in as an administrator, manage pizzas and users.
- Administrators can also export the pizza catalog as a PDF document.

---

## Running the tests

Execute all tests:

```bash
./mvnw test
```

Generate the JaCoCo reports:

```bash
./mvnw verify
```

The reports are generated in:

```
target/site/jacoco/index.html

delivery-service/target/site/jacoco/index.html
```

---

# Future Improvements

The project was designed to be easily extendable.

Possible future improvements include:

- Docker support
- Docker Compose configuration
- REST API documentation with Swagger / OpenAPI
- Email notifications
- Order payment integration
- Order history filtering
- Pagination for large datasets
- Cloud deployment

---

# Author

**Teodor Radev**

Spring Advanced Individual Project

SoftUni – June 2026

GitHub Repository:

https://github.com/RadevTT/pizza-delivery-app

---

# License

This project was developed for educational purposes as part of the SoftUni Spring Advanced course.