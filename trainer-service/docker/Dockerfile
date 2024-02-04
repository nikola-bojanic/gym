FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY build/libs/trainer-service.jar .
EXPOSE 8081
CMD ["java", "-jar", "trainer-service.jar", "--spring.profiles.active=docker"]