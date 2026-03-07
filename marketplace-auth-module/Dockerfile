# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
# Copia wrapper gradle
COPY gradlew .
COPY gradle ./gradle
# Copia config gradle
COPY settings.gradle .
COPY build.gradle .
# Copia modulo
COPY marketplace-auth-module ./marketplace-auth-module
# Fix permessi + CRLF
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
# Build modulo
RUN ./gradlew :marketplace-auth-module:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-auth-module/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8081} -jar app.jar"]