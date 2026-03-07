# Stage 1: Build
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
# File comuni dalla root
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
# File specifici del modulo Backend
COPY marketplace-backend-api/build.gradle marketplace-backend-api/
COPY marketplace-backend-api/src marketplace-backend-api/src
RUN chmod +x ./gradlew
RUN ./gradlew :marketplace-backend-api:build -x test

# Stage 2: Runtime
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-backend-api/build/libs/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${BACKEND_PORT:-8082} -jar app.jar"]