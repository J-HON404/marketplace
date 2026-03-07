# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
# Copia gradle wrapper
COPY gradlew .
COPY gradle ./gradle
# Copia configurazioni gradle
COPY settings.gradle .
COPY build.gradle .
# Copia modulo gateway completo
COPY marketplace-gateway-api ./marketplace-gateway-api
# Permessi + fix CRLF
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
# Build del modulo
RUN ./gradlew :marketplace-gateway-api:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-gateway-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -jar app.jar"]