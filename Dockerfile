FROM tomcat:10.1.17-jdk21-temurin-jammy

WORKDIR /usr/local/tomcat

COPY build/libs/spring-core-task.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]