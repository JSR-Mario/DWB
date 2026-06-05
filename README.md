<div align="center">

# E-Commerce Microservices Architecture (Backend)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.1.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</div>

Final project for the Web Backend Development course (UNAM). This repository contains the source code for a complete e-commerce backend, implementing a shopping cart, invoicing (checkout), and a modern microservices infrastructure using Spring Cloud and Docker.

---

## Architecture Overview

```text
                          ┌─────────────────┐
                          │   Admin (9091)  │
                          │  Spring Boot    │
                          │  Admin Server   │
                          └────────┬────────┘
                                   │ monitors
┌──────────────┐          ┌────────▼────────┐
│ Config (8888)│◄─────────│ Registry (8761) │
│ Cloud Config │  eureka  │  Eureka Server  │
└──────────────┘          └────────▲────────┘
                                   │ registers
              ┌────────────────────┼────────────────────┐
              │                    │                    │
     ┌────────┴───────┐  ┌────────┴───────┐  ┌────────┴───────┐
     │  Auth (8082)   │  │ Product (8080) │  │ Invoice (8084) │
     │  JWT / Users   │  │  Catalog/Stock │  │  Cart/Invoice  │
     └────────────────┘  └────────────────┘  └────────────────┘
              ▲                    ▲                    ▲
              │                    │                    │
              └────────────────────┴────────────────────┘
                          ┌────────┴────────┐
                          │  Gateway (9090) │
                          │  Cloud Gateway  │
                          └─────────────────┘
                                   ▲
                            Client Requests
```

---

## Tech Stack

- **Language:** Java 21
- **Core Framework:** Spring Boot 4.0.3
- **Cloud Ecosystem:** Spring Cloud 2025.1.1 (Eureka, Config Server, API Gateway)
- **Security:** Spring Security + JWT (jjwt 0.11.5 with HMAC-SHA256)
- **Persistence:** Spring Data JPA + Hibernate ORM + MySQL
- **Deployment:** Docker and Docker Compose
- **Documentation:** Springdoc OpenAPI / Swagger UI
- **Monitoring:** Spring Boot Admin and Spring Boot Actuator

---

## Local Deployment (Docker)

The project is fully dockerized. We use `network_mode: "host"` in Docker Compose so that the containers can natively communicate with your local MySQL installation without dealing with complex virtual IPs.

### Prerequisites
1. **Docker** and **Docker Compose** installed.
2. **MySQL** running on your local machine on port `3306`.
3. Create the `dwb_database` with username `root` and password `root`.
4. Run the DDL scripts provided in the class repositories to create the base tables.

### Starting the Project

To build the images and start all microservices in the background, run this command from the root directory:

```bash
docker compose up -d --build
```

### Viewing the Logs

To watch the services starting up in real-time (useful to see when Eureka has registered everything):
```bash
docker compose logs -f
```

Or for a specific service:
```bash
docker compose logs -f product-service
```

### Stopping the Services

```bash
docker compose down
```

---

## Testing the API (Main Endpoints)

**Important Note:** All client requests must be made through the API Gateway (`port 9090`), which handles routing to the correct microservice.

### 1. Authentication (Auth Service)
- **Register user:** `POST http://localhost:9090/user`
- **Login (Get JWT):** `POST http://localhost:9090/login`

### 2. Catalog (Product Service)
- **List products:** `GET http://localhost:9090/product`
- **Product detail by GTIN:** `GET http://localhost:9090/product/gtin/{gtin}`

### 3. Cart & Checkout (Invoice Service)
- **Add to cart:** `POST http://localhost:9090/cart-item` (Requires Header `Authorization: Bearer <token>`)
- **View cart:** `GET http://localhost:9090/cart-item`
- **Checkout:** `POST http://localhost:9090/invoice`
  - *Calculates totals with 16% tax.*
  - *Automatically decreases stock in the Product Service.*
  - *Generates the invoice and clears the cart.*

---

## Interactive Documentation (Swagger)

Once the project is running, you can access the interactive API documentation in your browser:
- **Product API:** http://localhost:8080/swagger-ui/index.html
- **Auth API:** http://localhost:8082/swagger-ui/index.html
- **Invoice API:** http://localhost:8084/swagger-ui/index.html

## Monitoring Dashboards

- **Eureka (Service Discovery):** http://localhost:8761
- **Spring Boot Admin:** http://localhost:9091
