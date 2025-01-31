# sanvalero-2dam-firsteval
Project made by Jorge Muniesa, Javier Planas and Javier Sanz for San Valero Foundation

## DB deployment
To deploy the database, you have to create a new file called `.env` in the devops folder with the following content:
```
# MySQL Configuration
MYSQL_ROOT_PASSWORD=S3cr3tP4ssw0rd
MYSQL_DATABASE=films_db
MYSQL_USER=user
MYSQL_PASSWORD=pass
MYSQL_PORT=3306
```
Then, you have to run the following command, placed in the devops folder:
```
docker compose up -d
```

## API's DB connection
To configure the API connection with the DB, you have to create a new file called `application.properties` in the following path: `/api/src/main/resources/`. This file should have the following content:
```
spring.application.name=example_name
spring.datasource.url=jdbc:mysql://localhost:example_port/example_db?serverTimezone=UTC
spring.datasource.username=example_user
spring.datasource.password=example_pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```
