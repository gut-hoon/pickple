# Build stage
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build application
RUN gradle clean build -x test

# Production stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Install curl for health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy jar from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (adjust if your Spring Boot uses different port)
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]