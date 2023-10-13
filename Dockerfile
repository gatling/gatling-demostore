FROM gatlingcorp/openjdk-base:21-jre-headless AS builder

COPY ./demo-store-*.jar /app/demo-store.jar

# Doing this modifies a lot of files, duplicating the content of /app in two layers: COPY above and RUN below,
# hence the builder image.
RUN chmod -R g=u /app

FROM gatlingcorp/openjdk-base:21-jre-headless
LABEL gatling="demostore"

COPY --from=builder --chown=1001:0 /app /app

WORKDIR /app
ENV HOME=/app
USER 1001

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/demo-store.jar"]
CMD []
