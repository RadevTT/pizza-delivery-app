# Pizza Delivery Application

## Overview

Pizza Delivery Application is a Spring Boot web application for managing pizzas and customer orders.

The application supports user registration and authentication, role-based authorization, pizza management, order creation, order tracking, and order administration.

---

## Features

### Authentication & Authorization

* User registration
* User login
* User logout
* Password encryption using BCrypt
* Spring Security integration
* Role-based access control
* USER and ADMIN roles

### Pizza Management (ADMIN)

* Add pizza
* Edit pizza
* Delete pizza
* Upload pizza image URL
* Manage pizza menu

### Orders

* Create order
* View personal orders
* View order details
* Track order status

### Admin Panel

* View all orders
* View order details
* Manage order statuses
* Update order workflow

### User Profile

* View profile information
* Display user roles

### Error Handling

* Username already exists validation
* Access denied page
* Global exception handling

---

## Functionalities

The application supports the following domain functionalities:

1. Create Pizza (ADMIN)
2. Edit Pizza (ADMIN)
3. Delete Pizza (ADMIN)
4. Create Order (USER)
5. Change Order Status (ADMIN)

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
* View order details
* View profile

### ADMIN

* All USER permissions
* Add pizzas
* Edit pizzas
* Delete pizzas
* View all orders
* View order details
* Change order statuses

Role restrictions are enforced through Spring Security.

Unauthorized users are redirected to a custom Access Denied page.

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

## Administrator Access

The application automatically creates the USER and ADMIN roles.

To test administrator functionality:

1. Register a user account.
2. Assign the ADMIN role to the user in the database.
3. Log in again.
4. The Admin Panel will become available from the dashboard.

---

## Validation

The application uses Jakarta Validation for form validation.

Examples:

* Required fields validation
* Username length validation
* Unique username validation
* Unique email validation
* Password strength validation
* Password confirmation validation
* Pizza name validation
* Price validation
* Quantity validation

---

## Error Handling

The application includes:

* UsernameAlreadyExistsException
* GlobalExceptionHandler
* Custom Access Denied page
* Validation error handling

---

## Author

Teodor Radev

Spring Fundamentals Individual Project

SoftUni – May 2026
