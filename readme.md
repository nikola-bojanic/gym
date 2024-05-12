NOTE - Execute docker-compose commands from docker folder.


1. When you want to run the application for the first time, use "docker-compose up --build" to build application image and to run it as a container on docker.

2. When running application any next time just use "docker-compose up" or "SPRING_PROFILES_ACTIVE={profile name} docker-compose up" if you want to run the application in a different spring profile.

3. Access API documentation in your web browser: http://localhost:8080/spring-core-task/swagger-ui/index.html

4. Access endpoints using Postman on Curl. Endpoint information is available on URL provided on step 3.