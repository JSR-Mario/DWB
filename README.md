<div align="center">

# E-Commerce Microservices Architecture (Backend)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.1.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

</div>

Central repository for the Web Backend Development course (UNAM). This project implements a complete e-commerce backend with shopping cart, invoicing, and a full microservices infrastructure (Registry, Config, Gateway, Admin).

---

## Architecture Overview

```
                          ┌─────────────────┐
                          │   Admin (9091)   │
                          │  Spring Boot     │
                          │  Admin Server    │
                          └────────┬─────────┘
                                   │ monitors
┌──────────────┐          ┌────────▼─────────┐
│ Config (8888)│◄─────────│ Registry (8761)  │
│ Cloud Config │  eureka  │  Eureka Server   │
└──────────────┘          └────────▲─────────┘
                                   │ registers
              ┌────────────────────┼────────────────────┐
              │                    │                     │
     ┌────────┴───────┐  ┌────────┴───────┐  ┌─────────┴──────┐
     │  Auth (8082)   │  │ Product (8080) │  │ Invoice (8084) │
     │  JWT / Users   │  │  Catalog/Stock │  │  Cart/Invoice  │
     └────────────────┘  └────────────────┘  └────────────────┘
              ▲                    ▲                     ▲
              │                    │                     │
              └────────────────────┴─────────────────────┘
                          ┌────────┴─────────┐
                          │  Gateway (9090)  │
                          │  Cloud Gateway   │
                          └──────────────────┘
                                   ▲
                              Client Requests
```

---

## Services & Ports

| Service          | Port | Description                                  |
|------------------|------|----------------------------------------------|
| Registry Service | 8761 | Eureka Server — service discovery            |
| Config Service   | 8888 | Spring Cloud Config — centralized config     |
| Auth Service     | 8082 | JWT authentication & user management         |
| Product Service  | 8080 | Product catalog, categories, images & stock  |
| Invoice Service  | 8084 | Shopping cart & invoice/checkout             |
| Gateway Service  | 9090 | API Gateway — single entry point for clients |
| Admin Service    | 9091 | Spring Boot Admin — monitoring dashboard     |

---

## Tech Stack

- **Language:** Java 21
- **Core Framework:** Spring Boot 4.0.3
- **Cloud:** Spring Cloud 2025.1.1 (Eureka, Config, Gateway)
- **Security:** Spring Security + JWT (jjwt 0.11.5, HMAC-SHA256)
- **Data:** Spring Data JPA + Hibernate ORM + MySQL (`dwb_database`)
- **Build:** Maven Wrapper (`mvnw`)
- **Docs:** Springdoc OpenAPI / Swagger UI
- **Monitoring:** Spring Boot Admin + Actuator

---

## Core Features

### Auth Service (`/auth-service/auth` — port 8082)
- `POST /user` — User registration (default role: `CUSTOMER`)
- `POST /login` — JWT token generation (1 hour expiration)
- `GET /user` — List all users (`Administrator` only)

### Product Service (`/product` — port 8080)
- **Categories:** Full CRUD with enable/disable (soft delete)
- **Products:** Full CRUD, search by ID or GTIN, enable/disable
- **Product Images:** Base64 upload, list, delete
- **Stock:** `PATCH /product/gtin/{gtin}/stock` — update stock quantities
- **RBAC:** `ADMIN` full access; `CUSTOMER` read-only on active items

### Invoice Service (`/invoice` — port 8084)
- **Shopping Cart:**
  - `POST /cart-item` — Add product to cart (validates stock via product service)
  - `GET /cart-item` — List cart items with product name, price, quantity
  - `DELETE /cart-item/{id}` — Remove specific item
  - `DELETE /cart-item` — Clear entire cart
- **Checkout:**
  - `POST /invoice` — Finalize purchase: validates stock, calculates totals (16% IVA), saves invoice, decrements stock, clears cart
  - `GET /invoice` — List invoices (admin: all, customer: own)
  - `GET /invoice/{id}` — Invoice detail with items

### Infrastructure Services
- **Registry (Eureka):** Service discovery at `http://localhost:8761`
- **Config Server:** Centralized properties at `http://localhost:8888`
- **Gateway:** Single entry point at `http://localhost:9090`
- **Admin:** Monitoring dashboard at `http://localhost:9091`

---

## Project Structure

```
DWB/
├── registry-service/               # Eureka Server (8761)
├── config-service/                 # Spring Cloud Config (8888)
├── gateway-service/                # API Gateway (9090)
├── admin-service/                  # Spring Boot Admin (9091)
│
├── auth-service/auth/              # Auth microservice (8082)
│   └── src/main/java/com/auth/
│       ├── controller/             # CtrlAuth, CtrlUser
│       ├── dto/in/                 # LoginRequest, UserRequest
│       ├── dto/out/                # UserResponse
│       ├── entity/                 # User (implements UserDetails)
│       ├── exception/              # GlobalExceptionHandler
│       ├── repo/                   # RepoUser
│       ├── security/               # SecurityConfig, JwtAuthFilter
│       ├── service/                # SvcUser/SvcUserImp, DefaultUserAuthentication
│       └── util/                   # JwtUtil
│
├── product/                        # Product microservice (8080)
│   └── src/main/java/com/product/
│       ├── api/controller/         # CtrlProduct, CtrlCategory
│       ├── api/dto/in/             # DtoProductIn, DtoCategoryIn, DtoStockIn
│       ├── api/dto/out/            # DtoProductOut, DtoProductListOut
│       ├── api/entity/             # Product, Category, ProductImage
│       ├── api/repository/         # RepoProduct, CategoryRepository
│       ├── api/service/            # SvcProduct/SvcProductImp, CategoryService/Impl
│       ├── common/mapper/          # MapperProduct
│       ├── config/jwt/             # JwtAuthFilter, JwtUtil
│       ├── config/security/        # SecurityConfig, CorsConfig
│       ├── config/openapi/         # OpenApiConfig
│       └── exception/              # ApiException, DBAccessException, RestExceptionHandler
│
├── invoice/                        # Invoice microservice (8084)
│   └── src/main/java/com/invoice/
│       ├── api/controller/         # CtrlInvoice, CtrlCartItem
│       ├── api/dto/in/             # DtoCartItemIn
│       ├── api/dto/out/            # DtoCartItemOut
│       ├── api/dto/                # ApiResponse, DtoInvoiceList, DtoProductResponse
│       ├── api/entity/             # Invoice, InvoiceItem, CartItem
│       ├── api/repository/         # RepoInvoice, RepoCartItem
│       ├── api/service/            # SvcInvoice/Imp, SvcCartItem/Imp
│       ├── commons/mapper/         # MapperInvoice
│       ├── commons/util/           # JwtDecoder
│       ├── config/                 # RestTemplateConfig
│       ├── config/jwt/             # JwtAuthFilter, JwtUtil, SecurityConfig, CorsConfig
│       ├── config/openapi/         # OpenApiConfig
│       └── exception/              # ApiException, DBAccessException, RestExceptionHandler
│
└── customer-service/               # Placeholder (not yet implemented)
```

---

## Local Development Setup

### Prerequisites
1. **Java 21** installed
2. **MySQL** running on port `3306`
3. Database `dwb_database` with credentials `root/root`
4. Run the DDL scripts in `invoice/src/main/resources/db/` to create cart and invoice tables

### Startup Order

Start services in this exact order:

```bash
# 1. Registry Service (wait until Eureka dashboard shows at http://localhost:8761)
cd registry-service && ./mvnw spring-boot:run

# 2. Config Service
cd config-service && ./mvnw spring-boot:run

# 3. Business Services (can start in parallel)
cd auth-service/auth && ./mvnw spring-boot:run
cd product && ./mvnw spring-boot:run
cd invoice && ./mvnw spring-boot:run

# 4. Gateway (once business services are registered in Eureka)
cd gateway-service && ./mvnw spring-boot:run

# 5. Admin (optional — monitoring dashboard)
cd admin-service && ./mvnw spring-boot:run
```

### Quick Test Flow (via Gateway — port 9090)

1. **Register a user:**
   ```
   POST http://localhost:9090/user
   ```
   ```json
   {
     "username": "customer1",
     "email": "customer1@example.com",
     "password": "Customer@123",
     "name": "Test",
     "lastName": "User",
     "phoneNumber": "5551234567"
   }
   ```

2. **Login:**
   ```
   POST http://localhost:9090/login
   ```
   ```json
   { "username": "customer1", "password": "Customer@123" }
   ```
   Response: `{"token": "eyJhbGciOi..."}`

3. **Add to cart** (use Bearer token):
   ```
   POST http://localhost:9090/cart-item
   Authorization: Bearer <token>
   ```
   ```json
   { "gtin": "7501055363513", "quantity": 2 }
   ```

4. **View cart:**
   ```
   GET http://localhost:9090/cart-item
   ```

5. **Checkout:**
   ```
   POST http://localhost:9090/invoice
   ```

6. **View invoices:**
   ```
   GET http://localhost:9090/invoice
   ```

### Swagger UI
- Product: http://localhost:8080/swagger-ui/index.html
- Invoice: http://localhost:8084/swagger-ui/index.html

### Monitoring
- Eureka Dashboard: http://localhost:8761
- Admin Dashboard: http://localhost:9091
