FROM maven:3-openjdk-17 AS build

COPY ./pom.xml /build/pom.xml

WORKDIR /build

RUN mvn dependency:resolve

COPY . /build

RUN mvn clean package

FROM azul/zulu-openjdk:17.0.6-jre-headless

COPY --from=build /build/target/demo-store-*.jar /demo-store.jar

CMD ["java", "-jar", "/demo-store.jar"]
