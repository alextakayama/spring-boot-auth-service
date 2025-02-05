# Use Azul Zulu OpenJDK 17.0.14 as the base image
FROM azul/zulu-openjdk:17.0.14

# Set the working directory inside the container
WORKDIR /app

# Copy all files from the current directory to the container's working directory
COPY . .

# Expose port 8080 for incoming connections
EXPOSE 8080

# Launches a Spring Boot application using Gradle wrapper
CMD ["./gradlew", "bootRun"]
