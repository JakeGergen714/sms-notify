# Use the official Maven image as a parent image
FROM maven:3.8.4-openjdk-17-slim as build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file into the container at /app
COPY ./pom.xml /app/pom.xml

# Copy the source code into the container at /app
COPY ./src /app/src

# Package the application
RUN mvn -f /app/pom.xml clean package

# For the final image, use the official OpenJDK image
FROM openjdk:17-slim

# Copy the JAR from the build stage to the final image
COPY --from=build /app/target/*.jar /usr/app/app.jar

# Set the working directory in the container to /usr/app
WORKDIR /usr/app

# Make port 8081 available to the world outside this container
EXPOSE 8082

ENV DATASOURCE_URL=jdbc:postgresql://postgres:5432/postgres

# Run the jar file 
ENTRYPOINT ["java", "-jar", "app.jar"]
