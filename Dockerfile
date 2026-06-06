FROM eclipse-temurin:21-jdk-alpine AS build

ARG SERVICE_DIR
WORKDIR /app

COPY ${SERVICE_DIR} /app/
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
