# Use the official gradle image as a parent image
FROM gradle:jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file into the container at /app
COPY ./build.gradle /app/build.gradle

# Copy the source code into the container at /app
COPY ./src /app/src

# Package the application
RUN gradle build

# For the final image, use the official OpenJDK image
FROM openjdk:17-slim

# Copy the JAR from the build stage to the final image
COPY --from=build /app/build/libs/*.jar /usr/app/app.jar

# Set the working directory in the container to /usr/app
WORKDIR /usr/app

# Make port 8090 available to the world outside this container
EXPOSE 8090

# Run the jar file 
ENTRYPOINT ["java", "-jar", "app.jar"]