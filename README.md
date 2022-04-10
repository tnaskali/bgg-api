# BGG-API

Spring Boot application acting as a bridge to
BoardGameGeek's [XML API 2](https://boardgamegeek.com/wiki/page/BGG_XML_API2) and Games Logging API. Its purpose is to
expose the same functionalities in a more user-friendly way using Json and OpenAPI.

# Features

- Static BGG XML API schemas in XSD format (located under [src/main/resources/schemas](src/main/resources/schemas))
- Json API for querying objects based on their XML API (no authentication required)
- Json API for logging games based on their Json API (basic authentication required)
- Swagger / OpenAPI3 specifications

# Usage

## build and run a java application locally

Prerequisites : have Java 11+ and maven installed on your machine

Steps :

1. clone this repository on your local machine
2. run `mvn spring-boot:run`
3. navigate to http://localhost:8088/bgg-api/swagger-ui.html

## build and run a native application locally

Prerequisites : have GraalVM (java11) 22+ and maven installed on your machine

Steps :

1. clone this repository on your local machine
2. run `mvn package -Pnative -DskipTests` to build the native image (takes about 10 minutes)
3. run `./target/bgg-api`
4. navigate to http://localhost:8088/bgg-api/swagger-ui.html

## pull and run a docker java image (Linux / MacOS only)

Prerequisites : have docker installed on your machine

Steps :

1. run `docker pull tnaskali/bgg-api:latest-snapshot` (or any other tag)
2. run `docker run --rm -p 8088:8088 tnaskali/bgg-api:latest-snapshot`
3. navigate to http://localhost:8088/bgg-api/swagger-ui.html

## pull and run a docker native image (Linux / MacOS only)

Prerequisites : have docker installed on your machine

Steps :

1. run `docker pull tnaskali/bgg-api-native:latest-snapshot` (or any other tag)
1. run `docker run --rm -p 8088:8088 tnaskali/bgg-api-native:latest-snapshot`
2. navigate to http://localhost:8088/bgg-api/swagger-ui.html

# A word about security

The games logging API will prompt for your BGG username and password. These will be transmitted in clear using unsecured
HTTP protocol from your browser to the locally running Spring Boot application and will only be kept in memory for the
duration of the session. Then the API will use a secure HTTPS connection to perform authentication to boardgamegeek.com.

# Terms of use

This is just a bridge to BoardGameGeek's API,
so [their terms of use](https://boardgamegeek.com/wiki/page/XML_API_Terms_of_Use#) still apply. Be sure to read them
before deciding to use this API.

# Credits and inspirations

- BGG's XML API 2 : https://boardgamegeek.com/wiki/page/BGG_XML_API2
- Fisico's thread on BGG forum : https://boardgamegeek.com/thread/1010057/xml-schema-for-bgg-xml-api2
- Reddit Thread on how to log plays programmatically : https://www.reddit.com/r/boardgames/comments/ez86me/uploading_games_plays_to_bgg_programmatically/
- Baeldung's tutorial on Spring Security Custom Authentication Provider : https://www.baeldung.com/spring-security-authentication-provider
