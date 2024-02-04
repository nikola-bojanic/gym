FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY build/libs/main-service.jar .
EXPOSE 8080
CMD ["java", "-jar", "main-service.jar", "--spring.profiles.active=docker"]