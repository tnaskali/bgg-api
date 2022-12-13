FROM eclipse-temurin:latest

ENV SERVER_PORT=80
EXPOSE ${SERVER_PORT}
WORKDIR /opt/bgg-api
ENTRYPOINT ["java","-jar","./bgg-api.jar"]

COPY target/bgg-api*.jar /opt/bgg-api/bgg-api.jar
