# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
# Copia gradle wrapper
COPY gradlew .
COPY gradle ./gradle
# Copia config gradle
COPY settings.gradle .
COPY build.gradle .
# Copia modulo backend
COPY marketplace-backend-api ./marketplace-backend-api
# Fix permessi + possibili CRLF
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
# Build del modulo
RUN ./gradlew :marketplace-backend-api:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-backend-api/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["sh","-c","java -Dserver.port=${BACKEND_PORT:-8082} -jar app.jar"]