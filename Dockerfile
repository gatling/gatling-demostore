FROM maven:3-openjdk-11 AS build

COPY ./pom.xml /build/pom.xml

WORKDIR /build

RUN mvn dependency:resolve

COPY . /build

RUN mvn clean package

FROM openjdk:11-jre-slim

COPY --from=build /build/target/demo-store-*.jar /demo-store.jar

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /demo-store.jar"]
