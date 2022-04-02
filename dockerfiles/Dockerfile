FROM eclipse-temurin:11

RUN mkdir /opt/bgg-api

COPY target/bgg-api*.jar /opt/bgg-api/bgg-api.jar

ENTRYPOINT ["java","-jar","/opt/app/bgg-api.jar"]