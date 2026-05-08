# ============================================================================
# Dockerfile برای Spring Boot 4.0.5 با اتصال به MySQL در Docker
# ============================================================================

# مرحله 1: ساخت (Build) با Maven
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ============================================================================

# مرحله 2: اجرا (Runtime) با JRE سبک
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]