# Stage 1: Build
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# Stage 2: Runtime
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
