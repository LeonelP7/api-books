FROM openjdk:21-slim
MAINTAINER lpereira.com
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

