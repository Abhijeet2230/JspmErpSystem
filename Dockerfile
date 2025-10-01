# ===========================
# Stage 1: Build with Maven
# ===========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ===========================
# Stage 2: Run the JAR
# ===========================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Set environment variables (can be overridden in docker-compose)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/jspm_jscoe
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV MINIO_URL=http://minio:9000
ENV MINIO_ACCESS_KEY=minioadmin
ENV MINIO_SECRET_KEY=minioadmin

# Copy JAR from builder stage (explicit name)
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]