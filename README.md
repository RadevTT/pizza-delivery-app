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

## Technologies

* Java 17
* Spring Boot
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

Entity relationships include:

* User ↔ Role
* User ↔ Order
* Order ↔ OrderItem
* OrderItem ↔ Pizza

Passwords are stored hashed using BCrypt.

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

Or use Maven:

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

Spring Boot Pizza Delivery Project
