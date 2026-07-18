# 🍕 Pizza Delivery Application

## Overview

Pizza Delivery Application is a Spring Boot web application for managing pizzas, customer orders and deliveries.

The project demonstrates a modern Spring Boot architecture including authentication, authorization, microservices communication, scheduling, caching, logging and unit testing.

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
- View pizza menu
- Pizza image support
- Cached pizza catalog using Spring Cache

---

## Orders

- Create order
- View personal orders
- View order details
- Automatic total price calculation
- Track order status

Order workflow:

- PENDING
- PREPARING
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED

---

## Delivery Microservice

The application communicates with a separate Delivery Service using Spring Cloud OpenFeign.

Features:

- Create delivery automatically after order creation
- Synchronize delivery status with order status
- Dispatch delivery
- Complete delivery
- Cancel delivery

---

## Scheduled Tasks

Automatic background processing using Spring Scheduler.

Implemented jobs:

- Automatically cancel expired pending orders
- Automatically dispatch delayed preparing orders

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
- Manage administrator roles
- Manage registered users
- Manage pizzas

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

The project uses SLF4J logging.

Important events are logged:

- User registration
- Profile updates
- Pizza management
- Order creation
- Order cancellation
- Order status changes
- Delivery status changes

---

## Caching

Spring Cache is used for:

- Pizza list
- Pizza lookup by ID

The cache is automatically invalidated after adding, editing or deleting pizzas.

---

## Resilience

Spring Cloud Circuit Breaker (Resilience4j)

Implemented fallback support for the Delivery Service.

If the Delivery Service is unavailable, the application fails gracefully without crashing.

---

# Technologies

- Java 17
- Spring Boot 3.4.0
- Spring Security
- Spring Data JPA
- Hibernate
- Spring Cache
- Spring Scheduler
- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker (Resilience4j)
- Thymeleaf
- MySQL
- Maven
- Bootstrap 5
- JUnit 5
- Mockito

---

# Database

The application uses MySQL.

All entities use UUID primary keys.

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
- View all orders
- Change order status
- Cancel orders
- Manage users
- Grant administrator role
- Remove administrator role

Access restrictions are enforced by Spring Security.

Unauthorized users are redirected to a custom Access Denied page.

---

# Testing

The project includes unit tests for the service layer using:

- JUnit 5
- Mockito

Covered services:

- PizzaService
- UserService
- OrderService

---

# Project Structure

```
src
├── config
├── controller
├── service
├── service.impl
├── scheduler
├── client
├── client.fallback
├── repository
├── model
│   ├── dto
│   ├── entity
│   └── enums
├── exception
├── templates
└── test
```

---

# Setup

## 1. Create database

```sql
CREATE DATABASE pizza_delivery;
```

## 2. Configure database

Edit:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizza_delivery
spring.datasource.username=root
spring.datasource.password=your_password
```

## 3. Start Delivery Service

Run:

```
delivery-service
```

Default port:

```
8081
```

## 4. Start Main Application

Run:

```
PizzaDeliveryApplication
```

Default port:

```
8080
```

---

# Default Roles

The application initializes automatically:

- USER
- ADMIN

---

# Administrator Access

1. Register a user.
2. Grant the ADMIN role.
3. Log in again.
4. The Administration panel becomes available.

---

# Author

**Teodor Radev**

Spring Advanced Individual Project

SoftUni – June 2026