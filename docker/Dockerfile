FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle/
COPY src ./src/

RUN gradle build

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/spring-core-task.jar .

EXPOSE 8080

CMD ["java", "-jar", "spring-core-task.jar", "--spring.profiles.active=docker"]