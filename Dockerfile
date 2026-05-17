FROM gcr.io/distroless/java25-debian13:latest

# Run as a non-root user with a well-known UID (65534 = nobody)
USER 65534:65534

WORKDIR /app

# Copy static wget from busybox
COPY --from=busybox:uclibc /bin/wget /usr/local/bin/wget

COPY target/bgg-api*.jar bgg-api.jar

EXPOSE 8080

HEALTHCHECK \
  --interval=30s \
  --timeout=5s \
  --start-period=10s \
  --retries=3 \
  CMD [ "wget", "-qO-", "http://localhost:8080/bgg-api/actuator/health" ]

ENTRYPOINT [ "java", "-jar", "bgg-api.jar" ]
