FROM maven:3-openjdk-17 AS builder

COPY ./pom.xml /build/pom.xml

WORKDIR /build

RUN mvn --batch-mode dependency:resolve

COPY . /build

RUN mvn --batch-mode clean package

RUN chmod -R g=u /build/target/demo-store-*.jar

FROM gatlingcorp/openjdk-base:17-jre-headless
LABEL gatling="demostore"

COPY --from=builder --chown=1001:0 /build/target/demo-store-*.jar /app/demo-store.jar

WORKDIR /app
ENV HOME=/app
USER 1001

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./demo-store.jar"]
CMD []
