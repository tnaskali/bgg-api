FROM gcr.io/distroless/java25-debian13:latest

ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}

WORKDIR /opt/bgg-api

# Run as a non-root user with a well-known UID (65534 = nobody).
# Distroless has no shell, so we use a pre-existing system user.
USER 65534:65534

ENTRYPOINT ["java","-jar","./bgg-api.jar"]

# Copy the JAR last — this layer changes most frequently,
# so it goes at the end to preserve caching for all previous layers.
COPY target/bgg-api*.jar /opt/bgg-api/bgg-api.jar
