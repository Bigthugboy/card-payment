# Use the official OpenJDK image from Docker Hub
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/*.jar /app/cardpayment-0.0.1-SNAPSHOT.jar.original

# Expose the port the Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/cardpayment-0.0.1-SNAPSHOT.jar.original"]
