NOTE - All of the shell commands except point 6 need to be executed from the project's root folder.

NOTE - Application container depends on latest postgres docker image. Application will create a new database(spring-rest) and a new user(user) and give him superuser privileges in postgres container. If you are using postgres:latest in docker and you don't want to create a new database and user in it, specify different postgres image version in docker-compose.yml file.

NOTE -Application is hosted localy on port 8080. Postgres is listening on local port 5433. If you would like to avoid this, specify different ports in docker-compose.yml. 

1. Test the application using gradle tasks - "./gradlew clean test".

2. Build the application war artifact using gradle task - "./gradlew build". You can also use gradle task - "./gradlew war" here if you'd like to.

3. Build a docker image and start serving the application on local tomcat using docker command - "docker-compose up --build". Use docker command - "docker-compose up --build" only for the first time when you are building the application docker image, in every other case use only "docker-compose up" to start a docker container.

4. Access API documentation in your web browser: http://localhost:8080/spring-core-task/swagger-ui/index.html

5. Access endpoints using Postman on Curl. Endpoint information is available on URL provided on step 4. 

6. To see database entries open terminal and execute docker command "docker exec -it {container-id/container-name} /bin/bash. After that you will be inside your postgres container environment. Inside postgres contatiner execute command "psql -U user spring-rest", and you will be connected to the spring-rest database as user "user".  
