1. Clone the repository to your local machine.
2. Build both services using commands "./gradlew :trainer-service:build" and "./gradlew :main-service:build" from the root folder.
3. Navigate to docker folder from root folder and use command "docker-compose up --build" to build and run the docker container.
4. Access the API documentation on "http://localhost:8080/swagger-ui/index.html".
5. Make requests to the application using HTTP calls over curl, postman or your web browser. 
