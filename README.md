# 🍕 Pizza Delivery Application

A full-stack Spring Boot web application for managing pizzas, customer
orders and deliveries with a dedicated Delivery microservice.

The project was developed as an individual assignment for the **SoftUni
Spring Advanced** course and demonstrates a modern Spring Boot
architecture using layered design, authentication, authorization,
microservices, Spring Events, AOP logging, scheduling, caching,
resilience patterns, PDF export and automated testing.

------------------------------------------------------------------------

# Architecture

The system consists of two Spring Boot applications:

-   **Pizza Delivery Application** -- the main web application
-   **Delivery Service** -- a separate microservice responsible for
    delivery management

The applications communicate through REST APIs using **Spring Cloud
OpenFeign**.

To improve reliability, the project uses **Spring Cloud Circuit Breaker
(Resilience4j)** with fallback support when the Delivery Service is
unavailable.

------------------------------------------------------------------------

# Features

## Authentication & Authorization

-   User registration
-   User login
-   User logout
-   BCrypt password encryption
-   Spring Security integration
-   Role-based authorization
-   USER and ADMIN roles

## Pizza Management (ADMIN)

-   Add pizza
-   Edit pizza
-   Delete pizza
-   Browse pizza menu
-   Pizza image support
-   Cached pizza catalog using Spring Cache
-   Export pizza menu as PDF

## Orders

-   Create order
-   View personal orders
-   View order details
-   Automatic total price calculation
-   Track order status

### Order workflow

-   PENDING
-   PREPARING
-   OUT_FOR_DELIVERY
-   DELIVERED
-   CANCELLED

## Delivery Microservice

-   Automatically create delivery after order creation
-   Synchronize delivery status with order status
-   Dispatch deliveries
-   Complete deliveries
-   Cancel deliveries

## Spring Events

-   OrderCreatedEvent
-   OrderCreatedEventListener

## Scheduled Tasks

-   Cancel expired pending orders
-   Dispatch delayed preparing orders

## User Profile

-   View profile
-   Edit email
-   Change password
-   Display assigned roles

## Administration

-   View all orders
-   Manage pizzas
-   Manage users
-   Grant or remove ADMIN role

## PDF Export

Authenticated administrators can export the pizza menu as a PDF document
directly from the pizza catalog.

## Logging

The project uses **SLF4J** together with **Spring AOP**. Service method
execution time and failures are logged automatically.

## Caching

-   Pizza list
-   Pizza lookup by ID

## Resilience

Spring Cloud Circuit Breaker (Resilience4j) with fallback support for
Delivery Service communication.

# Technologies

-   Java 17
-   Spring Boot 3.4.0
-   Spring MVC
-   Spring Security
-   Spring Data JPA
-   Hibernate
-   Spring Cache
-   Spring Events
-   Spring AOP
-   Spring Scheduler
-   Spring Cloud OpenFeign
-   Spring Cloud Circuit Breaker (Resilience4j)
-   Thymeleaf
-   MySQL
-   OpenPDF
-   Bootstrap 5
-   Maven
-   JUnit 5
-   Mockito
-   Spring Boot Test
-   MockMvc
-   JaCoCo

# Testing

Covered components include services, controllers, PDF export, Spring
Events, AOP logging and the Delivery Service.

Project statistics:

-   ✅ 65 automated tests
-   ✅ JaCoCo code coverage reports
-   ✅ 77% line coverage (main application)
-   ✅ 98% line coverage (delivery-service)

# Project Structure

``` text
pizza-delivery-application
├── src
│   ├── aspect
│   ├── client
│   ├── config
│   ├── controller
│   ├── event
│   ├── exception
│   ├── init
│   ├── model
│   ├── repository
│   ├── scheduler
│   ├── service
│   ├── service.impl
│   └── templates
└── delivery-service
```

# Setup

1.  Create databases:

``` sql
CREATE DATABASE pizza_delivery;
CREATE DATABASE pizza_delivery_service;
```

2.  Configure `application.properties` in both applications.

3.  Start `DeliveryServiceApplication` (port 8081).

4.  Start `PizzaDeliveryApplication` (port 8080).

5.  Log in as an administrator and use **Export PDF** from the pizza
    catalog.

6.  Run tests:

``` bash
./mvnw test
./mvnw verify
```

JaCoCo reports are generated in:

``` text
target/site/jacoco/index.html
delivery-service/target/site/jacoco/index.html
```

# Author

**Teodor Radev**

Spring Advanced Individual Project

SoftUni -- June 2026

# License

This project was developed for educational purposes as part of the
SoftUni Spring Advanced course.
