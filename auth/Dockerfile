# For the final image, use the official OpenJDK image
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /opt

COPY target/*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

ENV DATASOURCE_URL=jdbc:postgresql://postgres:5432/postgres

# Run the jar file 
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
