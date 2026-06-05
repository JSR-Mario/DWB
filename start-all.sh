#!/bin/bash

# start-all.sh - Inicia todos los microservicios en el orden correcto
# Preparado para KDE (Konsole) y zsh

echo "Iniciando infraestructura de Microservicios DWB..."

# 1. Registry
echo "Iniciando Registry Service (8761)..."
konsole --noclose -e zsh -c "cd registry-service && ./mvnw spring-boot:run" &

# Esperar unos segundos para que Eureka levante
sleep 15

# 2. Config
echo "Iniciando Config Service (8888)..."
konsole --noclose -e zsh -c "cd config-service && ./mvnw spring-boot:run" &

sleep 10

# 3. Servicios de Negocio (Product, Auth, Invoice)
echo "Iniciando Product Service (8080)..."
konsole --noclose -e zsh -c "cd product && ./mvnw spring-boot:run" &

echo "Iniciando Auth Service (8082)..."
konsole --noclose -e zsh -c "cd auth-service/auth && ./mvnw spring-boot:run" &

echo "Iniciando Invoice Service (8084)..."
konsole --noclose -e zsh -c "cd invoice && ./mvnw spring-boot:run" &

# Esperar para que se registren en Eureka
sleep 20

# 4. Gateway
echo "Iniciando Gateway Service (9090)..."
konsole --noclose -e zsh -c "cd gateway-service && ./mvnw spring-boot:run" &

# 5. Admin (Opcional)
echo "Iniciando Admin Service (9091)..."
konsole --noclose -e zsh -c "cd admin-service && ./mvnw spring-boot:run" &

echo "================================================="
echo "Todos los servicios han sido lanzados en ventanas de Konsole separadas."
echo "Puedes probar la aplicación a través del API Gateway en el puerto 9090."
echo "================================================="
