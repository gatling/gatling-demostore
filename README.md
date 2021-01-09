# Gatling DemoStore Setup

## Database setup

The application requires a mysql database to be running on the localhost

All of the required tables for the database can be found in `database-dump.sql`

For the application to connect to the database, the password `youcantguessme` should be set as a property, i.e. `export jasypt_encryptor_password=youcantguessme`

## Running the application

- To run the application through Spring Boot do  `mvn spring-boot:run -Dspring-boot.run.arguments=--jasypt.encryptor.password=youcantguessme`

- Or build the project and run the jar with `java -jar -Djasypt.encryptor.password=youcantguessme demo-store-0.0.1-SNAPSHOT.jar`

