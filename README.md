# Proyecto: Desarrollo Web Backend (DWB)

Bienvenido al repositorio central de la asignatura Desarrollo Web Backend (UNAM). Este proyecto es un ecosistema basado en una arquitectura de microservicios independientes que trabajan en conjunto para gestionar la logica de negocio de la plataforma.

---

## Tech Stack

El proyecto esta construido siguiendo los estandares de la industria para desarrollo en Java:

*   Lenguaje: Java 21
*   Framework Core: Spring Boot 4.x
*   Seguridad: Spring Security + JSON Web Tokens (JWT)
*   Persistencia: Spring Data JPA + Hibernate
*   Base de Datos: MySQL
*   Construccion y Dependencias: Maven Wrapper (mvnw)
*   Validaciones: Jakarta Validation (spring-boot-starter-validation)

---

## Microservicios y Funcionalidades

### 1. Auth Service (/auth-service)
*   Puerto de ejecucion: 8082
*   Responsabilidad: Motor principal de autenticacion, control de acceso y generacion de credenciales JWT.
*   Funcionalidades Destacadas:
    *   POST /user: Registro seguro de nuevos usuarios (las contraseñas se cifran mediante BCryptPasswordEncoder).
    *   POST /login: Validacion de credenciales y generacion del token JWT firmado con algoritmo HMAC-SHA256.

### 2. Product Service (/product)
*   Puerto de ejecucion: 8080
*   Responsabilidad: Administracion de todo el catalogo, inventario y agrupacion de categorias.
*   Seguridad: Endpoints interceptados por un JwtAuthFilter. Permite acceso diferenciado entre roles ADMIN y CUSTOMER.
*   Funcionalidades Destacadas:
    *   Catalogo y Categorias: CRUD completo. Manejo estricto de unicidad de campos (GTIN, Nombres, Tags).
    *   Gestor de Archivos (Imagenes): Las imagenes se guardan de manera fisica en el disco (uploads/img/) e internamente se procesan y devuelven como tramas Base64 via JPQL custom querys (INNER JOIN).

---

## Como Correr el Proyecto (Entorno Local)

### Requisitos Previos
1.  Java 21 configurado en tus variables de entorno.
2.  Servidor MySQL corriendo en el puerto 3306.
3.  Una base de datos llamada dwb_database con usuario root y contraseña root (Configuracion por defecto del application-local.properties).

### Pasos de Ejecucion

Dado que es una arquitectura distribuida, necesitas levantar cada microservicio en su propia terminal.

Paso 1: Levantar el Servicio de Autenticacion
```bash
cd auth-service/auth
./mvnw clean spring-boot:run
```

Paso 2: Levantar el Servicio de Productos
```bash
cd product
./mvnw clean spring-boot:run
```

Paso 3: Crear tu Identidad (Postman)
1. Haz un POST a http://localhost:8082/user para registrar tus datos.
2. Nota de Administrador: Por defecto el sistema te da el rol "User". Ve a tu gestor de base de datos (ej. DBeaver) y ejecuta UPDATE user_roles SET roles = 'ADMIN' WHERE user_id = 1; para obtener permisos totales.
3. Haz un POST a http://localhost:8082/login con tus credenciales. Obtendras tu Token JWT.

Paso 4: Consumir la API Segura
Copia el Token que acabas de generar, ve a cualquier peticion de tu Product service (ej. GET http://localhost:8080/product/1), en la pestaña Authorization selecciona Bearer Token, pega el codigo y listo.
