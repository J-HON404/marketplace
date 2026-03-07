# Stage 1: Build
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
# File comuni dalla root
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
# File specifici del modulo Gateway
COPY marketplace-gateway-api/build.gradle marketplace-gateway-api/
COPY marketplace-gateway-api/src marketplace-gateway-api/src
RUN chmod +x ./gradlew
RUN ./gradlew :marketplace-gateway-api:build -x test

# Stage 2: Runtime
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-gateway-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]