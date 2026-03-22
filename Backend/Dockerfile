FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src src
RUN ./mvnw package -DskipTests
# Diagnostic step to see what was actually built
RUN ls -l target/

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy the executable jar produced with classifier 'exec'
COPY --from=build /app/target/app-exec.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
