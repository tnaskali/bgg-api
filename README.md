# BGG-API

Spring Boot application acting as a proxy to
BoardGameGeek's [XML API](https://boardgamegeek.com/wiki/page/BGG_XML_API), [XML API 2](https://boardgamegeek.com/wiki/page/BGG_XML_API2),
as well as their undocumented Json API. Its purpose is to expose the same functionalities, mainly retrieving but also
persisting data, in a more user-friendly and developer-friendly way.

# Features

- Static BGG XML API schemas in XSD format (located under [src/main/xsd](src/main/xsd))
- Proxied XML and Json API for querying data based on their public API (no authentication required)
- Proxied Json API for mutating data (e.g. logging games) based on their public API (basic authentication required)
- OpenAPI definition and Swagger UI (/bgg-api/swagger-ui.html) web interface
- Support for building both Java and native artifacts and images

# Usage

## build and run a java application locally

Prerequisites : have Java 17+ and maven installed on your machine

Steps :

1. clone this repository on your local machine
2. run `mvn spring-boot:run`
3. navigate to http://localhost:8080/bgg-api/actuator/health

## build and run a native application locally

Prerequisites : have GraalVM 22+ (java17) and maven installed on your machine

Steps :

1. clone this repository on your local machine
2. run `mvn native:compile -Pnative` to build the native image (takes about 5 minutes)
3. run `./target/bgg-api`
4. navigate to http://localhost:8080/bgg-api/actuator/health

## pull and run a docker java image (Linux / MacOS only)

Prerequisites : have docker installed on your machine

Steps :

1. run `docker pull ghcr.io/tnaskali/bgg-api:master` (or any other tag)
2. run `docker run --rm -p 8080:80 tnaskali/bgg-api:master`
3. navigate to http://localhost:8080/bgg-api/actuator/health

## pull and run a docker native image (Linux / MacOS only)

Prerequisites : have docker installed on your machine

Steps :

1. run `docker pull ghcr.io/tnaskali/bgg-api-native:master` (or any other tag)
2. run `docker run --rm -p 8080:8080 tnaskali/bgg-api-native:master`
3. navigate to http://localhost:8080/bgg-api/actuator/health

# A word about security

The games logging API will prompt for your BGG username and password. These will be transmitted in clear using unsecured
HTTP protocol from your browser to the locally running Spring Boot application and will only be kept in memory for the
duration of the session. Then the API will use a secure HTTPS connection to perform authentication to boardgamegeek.com.

# Terms of use

This is just a proxy to BoardGameGeek's API, so
[their terms of use](https://boardgamegeek.com/wiki/page/XML_API_Terms_of_Use#) still apply. Be sure to read them
before deciding to use this API.

# Credits and inspirations

- BGG's XML API 2 : https://boardgamegeek.com/wiki/page/BGG_XML_API2
- Fisico's thread on BGG forum : https://boardgamegeek.com/thread/1010057/xml-schema-for-bgg-xml-api2
- Reddit Thread on how to log plays
  programmatically : https://www.reddit.com/r/boardgames/comments/ez86me/uploading_games_plays_to_bgg_programmatically/
- Baeldung's tutorial on Spring Security Custom Authentication
  Provider : https://www.baeldung.com/spring-security-authentication-provider
- shaikezr's solution to caching Mono<?> : https://github.com/shaikezr/async-cacheable (until https://github.com/spring-projects/spring-framework/issues/17920 gets released)
