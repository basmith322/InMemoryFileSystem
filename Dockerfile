# Stage 1: Compile and test
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build and run tests
RUN mvn clean verify

# Stage 2: Run
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/classes /app

ENTRYPOINT ["java", "filesystem.FilesystemCLI"]