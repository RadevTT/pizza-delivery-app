# Pizza Delivery Application

## Overview

Pizza Delivery Application is a Spring Boot web application for managing pizzas and customer orders.

The application supports user registration and authentication, role-based authorization, pizza management, order creation, and order administration.

---

## Features

### Authentication & Authorization

* User registration
* User login
* User logout
* Password encryption using BCrypt
* Spring Security integration
* USER and ADMIN roles

### Pizza Management

* Add pizza
* Edit pizza
* Delete pizza
* View all pizzas

### Orders

* Create order
* View personal orders
* View order details
* Track order status

### Admin Panel

* View all orders
* Manage order statuses
* Update order workflow

### User Profile

* View profile information
* Display user roles

---

## Functionalities

The application supports the following domain functionalities:

1. Create Pizza
2. Edit Pizza
3. Delete Pizza
4. Create Order
5. Change Order Status (Admin)

---

## Technologies

* Java 17
* Spring Boot 3.4.0
* Spring Security
* Spring Data JPA
* Hibernate
* Thymeleaf
* MySQL
* Maven
* Bootstrap 5

---

## Database

The application uses MySQL as the primary database.

All entities use UUID as primary keys.

### Entity Relationships

* User ↔ Role
* User ↔ Order
* Order ↔ OrderItem
* OrderItem ↔ Pizza

Passwords are stored hashed using BCrypt.

---

## Security

### Guest

* Register
* Login

### USER

* View pizzas
* Create orders
* View personal orders
* View profile

### ADMIN

* All USER permissions
* View all orders
* Change order statuses

---

## Order Workflow

Orders can move through the following statuses:

* PENDING
* PREPARING
* DELIVERED

Only administrators can manage order statuses.

---

## Project Structure

```text
src
├── controller
├── service
├── service.impl
├── repository
├── model
│   ├── entity
│   ├── dto
│   └── enums
├── config
├── exception
└── templates
```

---

## Setup

### 1. Create Database

```sql
CREATE DATABASE pizza_delivery;
```

### 2. Configure Database Credentials

Edit:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizza_delivery
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Run Application

Using IntelliJ IDEA:

* Open project
* Load Maven dependencies
* Run PizzaDeliveryApplication

Or use Maven Wrapper:

```bash
./mvnw spring-boot:run
```

---

## Default Roles

The application automatically initializes:

* USER
* ADMIN

during startup.

---

## Author

Teodor Radev

Spring Fundamentals Individual Project
SoftUni – May 2026
