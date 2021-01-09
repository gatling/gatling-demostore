Gatling DemoStore Setup

Source code -> https://github.com/james-willett/gatling-spring-demostore

- The application is a Spring Boot project
- To run the application through Spring Boot do  `mvn spring-boot:run -Dspring-boot.run.arguments=--jasypt.encryptor.password=youcantguessme`

- Or build the project and run the jar with `java -jar -Djasypt.encryptor.password=youcantguessme demo-store-0.0.1-SNAPSHOT.jar`