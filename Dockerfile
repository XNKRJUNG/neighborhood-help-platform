# Use Java 17 base image
FROM amazoncorretto:17-alpine
LABEL author="dabusa"

# Set working directory inside the container
WORKDIR /app

# Copy the fat JAR into the container
COPY build/libs/nhp-1.0.1.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
