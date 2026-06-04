#!/bin/bash

# start-all.sh - Inicia todos los microservicios en el orden correcto
# Detener el script si hay algun error
set -e

echo "Iniciando infraestructura de Microservicios DWB..."

# 1. Registry
echo "Iniciando Registry Service (8761)..."
gnome-terminal -- bash -c "cd registry-service && ./mvnw spring-boot:run; exec bash"

# Esperar unos segundos para que Eureka levante
sleep 15

# 2. Config
echo "Iniciando Config Service (8888)..."
gnome-terminal -- bash -c "cd config-service && ./mvnw spring-boot:run; exec bash"

sleep 10

# 3. Servicios de Negocio (Product, Auth, Invoice)
echo "Iniciando Product Service (8080)..."
gnome-terminal -- bash -c "cd product && ./mvnw spring-boot:run; exec bash"

echo "Iniciando Auth Service (8082)..."
gnome-terminal -- bash -c "cd auth-service/auth && ./mvnw spring-boot:run; exec bash"

echo "Iniciando Invoice Service (8084)..."
gnome-terminal -- bash -c "cd invoice && ./mvnw spring-boot:run; exec bash"

# Esperar para que se registren en Eureka
sleep 20

# 4. Gateway
echo "Iniciando Gateway Service (9090)..."
gnome-terminal -- bash -c "cd gateway-service && ./mvnw spring-boot:run; exec bash"

# 5. Admin (Opcional)
echo "Iniciando Admin Service (9091)..."
gnome-terminal -- bash -c "cd admin-service && ./mvnw spring-boot:run; exec bash"

echo "================================================="
echo "Todos los servicios han sido lanzados en terminales separadas."
echo "Puedes probar la aplicacion a traves del API Gateway en el puerto 9090."
echo "Para instrucciones completas de prueba, revisa el README.md"
echo "================================================="
