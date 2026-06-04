<!-- 
<div align="center">
  <img src="path/to/your/project-banner-or-logo.png" alt="Project Banner" width="100%">
</div> 
-->

<div align="center">

# E-Commerce Microservices Architecture (Backend)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

</div>

Central repository for the Web Backend Development course (UNAM). This project demonstrates a robust, scalable backend ecosystem built on an independent microservices architecture, working concurrently to manage core business logic.

---

## Architecture & Tech Stack

This project is built following modern industry standards for enterprise Java development:

*   **Language:** Java 21
*   **Core Framework:** Spring Boot 4.0.3
*   **Security:** Spring Security with Stateless JSON Web Tokens (JWT via jjwt 0.11.5)
*   **Data Persistence:** Spring Data JPA + Hibernate ORM
*   **Database:** MySQL (`dwb_database`)
*   **Build Tool:** Maven Wrapper (`mvnw`)
*   **Validation:** Jakarta Validation (`spring-boot-starter-validation`)

---

## Microservices & Core Features

### 1. Auth Service (`/auth-service/auth`)
*   **Port:** `8082`
*   **Config:** `application.yaml`
*   **Role:** Identity Provider (IdP) and Authorization Server. Handles user management and JWT credential generation.
*   **Key Features:**
    *   `POST /user` — Secure user registration with password encryption (`BCryptPasswordEncoder`) and field validation (email format, password strength, phone number format). Default role: `User`.
    *   `POST /login` — Credential validation and JWT generation (signed HMAC-SHA256). Token includes: `sub` (username), `id` (user ID), `roles`. Expires in 1 hour.
    *   `GET /user` — List all users (requires `Administrator` authority).
*   **Error Handling:** `GlobalExceptionHandler` handles validation errors, duplicate entries (`DataIntegrityViolationException`), bad credentials, and generic exceptions. Returns `Map<String, String>` bodies.
*   **Note:** Uses Lombok (`@Data`, `@Getter`, `@Setter`) for DTOs and entities.

### 2. Product Service (`/product`)
*   **Port:** `8080`
*   **Config:** `application.properties` + `application-local.properties`
*   **Role:** Resource Server managing the product catalog, inventory, and categorization.
*   **Security:** Endpoints secured via a custom `JwtAuthFilter`, implementing Role-Based Access Control (RBAC):
    *   **`ADMIN` + `CUSTOMER`:** `GET /category/active`, `GET /product/{id}`, `GET /product/{id}/image`
    *   **`ADMIN` only:** All other CRUD operations (products, categories, images)
*   **Key Features:**
    *   **Categories (`/category`):** Full CRUD with enable/disable (soft delete via `status`). Unique constraints on `category` name and `tag`.
    *   **Products (`/product`):** Full CRUD with enable/disable. Unique constraints on `gtin` and `product` name. Product detail includes category name via JPQL join.
    *   **Product Images (`/product/{id}/image`):** Upload (Base64 → PNG file), list, and delete. Images stored on filesystem at `uploads/img/product/{uuid}.png`.
*   **Error Handling:** `RestExceptionHandler` catches `ApiException`, `MethodArgumentNotValidException`, and generic exceptions. Returns structured `ExceptionResponse` objects.
*   **Note:** No Lombok — uses manual getters/setters.

### 3. Customer Service (`/customer-service`)
*   **Status:** Placeholder — not yet implemented.

---

## Project Structure

```
DWB/
├── auth-service/auth/              # Auth microservice
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
├── product/                        # Product microservice
│   └── src/main/java/com/product/
│       ├── api/
│       │   ├── controller/         # CtrlProduct, CtrlCategory
│       │   ├── dto/in/             # DtoProductIn, DtoCategoryIn, DtoProductImageIn
│       │   ├── dto/out/            # DtoProductOut, DtoProductListOut
│       │   ├── entity/             # Product, Category, ProductImage
│       │   ├── repository/         # RepoProduct, RepoProductImage, CategoryRepository
│       │   └── service/            # SvcProduct/SvcProductImp, SvcProductImage/SvcProductImageImp
│       │                           # CategoryService/CategoryServiceImpl
│       ├── common/mapper/          # MapperProduct
│       ├── config/
│       │   ├── jwt/                # JwtAuthFilter, JwtUtil
│       │   └── security/           # SecurityConfig, CorsConfig
│       └── exception/              # ApiException, DBAccessException,
│                                   # ExceptionResponse, RestExceptionHandler
│
└── customer-service/               # Placeholder (not yet implemented)
```

---

## Local Development Setup

### Prerequisites
1.  **Java 21** installed and configured in your environment variables.
2.  **MySQL Server** running on port `3306`.
3.  A local database named `dwb_database` configured with default credentials (`root` / `root`) or updated in the respective properties/yaml files.

### Execution Steps

Due to its distributed architecture, each microservice must be run in a separate terminal instance.

**Step 1: Start the Auth Service**
```bash
cd auth-service/auth
./mvnw clean spring-boot:run
```

**Step 2: Start the Product Service**
```bash
cd product
./mvnw clean spring-boot:run
```

**Step 3: Identity Creation (via Postman or similar client)**

1. Register a user:
   ```
   POST http://localhost:8082/user
   ```
   ```json
   {
     "username": "admin",
     "email": "admin@example.com",
     "password": "Admin@123",
     "name": "Admin",
     "lastName": "User",
     "phoneNumber": "5551234567"
   }
   ```
2. *Admin Note:* The system assigns the `"User"` role by default. To gain full access, manually update your role in the database (e.g., via DBeaver):
   ```sql
   UPDATE user_roles SET roles = 'ADMIN' WHERE user_id = 1;
   ```
3. Login to get your JWT:
   ```
   POST http://localhost:8082/login
   ```
   ```json
   {
     "username": "admin",
     "password": "Admin@123"
   }
   ```
   Response: `{"token": "eyJhbGciOi..."}`

**Step 4: Consuming the Secure API**

Copy the generated token. For any subsequent request to the Product service (e.g., `GET http://localhost:8080/product/1`), go to the **Authorization** tab, select **Bearer Token**, paste your token, and execute the request.
