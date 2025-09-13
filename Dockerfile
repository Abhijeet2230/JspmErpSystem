# Use an OpenJDK image
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/jspm_jscoe?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Copy jar into container
ARG JAR_FILE=target/admissionportal-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
