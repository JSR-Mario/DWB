FROM eclipse-temurin:21-jdk-alpine AS build

# Argumento que recibe la ruta del microservicio (ej. "product" o "auth-service/auth")
ARG SERVICE_DIR
WORKDIR /app

# Copiamos todo el código fuente del microservicio especificado
COPY ${SERVICE_DIR} /app/

# Damos permisos al wrapper y compilamos el proyecto ignorando las pruebas
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa 2: Imagen final super ligera (JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos únicamente el JAR compilado de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Por defecto corremos la app
ENTRYPOINT ["java", "-jar", "app.jar"]
