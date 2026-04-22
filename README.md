<!-- 
<div align="center">
  <img src="path/to/your/project-banner-or-logo.png" alt="Project Banner" width="100%">
</div> 
-->

<div align="center">

# E-Commerce Microservices Architecture (Backend)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

</div>

Central repository for the Web Backend Development course (UNAM). This project demonstrates a robust, scalable backend ecosystem built on an independent microservices architecture, working concurrently to manage core business logic.

---

## Architecture & Tech Stack

This project is built following modern industry standards for enterprise Java development:

*   **Language:** Java 21
*   **Core Framework:** Spring Boot 4.x
*   **Security:** Spring Security with Stateless JSON Web Tokens (JWT)
*   **Data Persistence:** Spring Data JPA + Hibernate ORM
*   **Database:** MySQL
*   **Build Tool:** Maven Wrapper (`mvnw`)
*   **Validation:** Jakarta Validation (`spring-boot-starter-validation`)

---

## Microservices & Core Features

### 1. Auth Service (`/auth-service`)
*   **Port:** `8082`
*   **Role:** Identity Provider (IdP) and Authorization Server. Handles user management and JWT credential generation.
*   **Key Features:**
    *   `POST /user`: Secure user registration featuring password encryption via `BCryptPasswordEncoder`.
    *   `POST /login`: Credential validation and JWT generation (signed using HMAC-SHA256).

### 2. Product Service (`/product`)
*   **Port:** `8080`
*   **Role:** Resource Server managing the product catalog, inventory, and categorization.
*   **Security:** Endpoints are secured via a custom `JwtAuthFilter`, implementing Role-Based Access Control (RBAC) with differentiated access for `ADMIN` and `CUSTOMER` roles.
*   **Key Features:**
    *   **Catalog & Categories:** Full CRUD capabilities with strict field uniqueness constraints (GTIN, Names, Tags).
    *   **Media Management:** Physical filesystem storage for images (`uploads/img/`). Images are dynamically processed and returned as Base64 encoded strings via custom JPQL queries (`INNER JOIN`).

---

## Local Development Setup

### Prerequisites
1.  **Java 21** installed and configured in your environment variables.
2.  **MySQL Server** running on port `3306`.
3.  A local database named `dwb_database` configured with default credentials (`root` / `root`) or updated in the `application-local.properties` file.

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
1. Send a `POST` request to `http://localhost:8082/user` with your registration payload.
2. *Admin Note:* The system assigns the `"User"` role by default. To gain full access, manually update your role in the database (e.g., via DBeaver): `UPDATE user_roles SET roles = 'ADMIN' WHERE user_id = 1;`
3. Send a `POST` request to `http://localhost:8082/login` with your credentials to receive your **JWT Token**.

**Step 4: Consuming the Secure API**
Copy the generated token. For any subsequent request to the Product service (e.g., `GET http://localhost:8080/product/1`), go to the **Authorization** tab, select **Bearer Token**, paste your token, and execute the request.
