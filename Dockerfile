# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:20

RUN mkdir /app
# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container at /app
COPY build/libs/turnero-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application listens on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
