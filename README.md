# sanvalero-2dam-firsteval
Project made by Jorge Muniesa, Javier Planas and Javier Sanz for San Valero Foundation

## How to run the project

### DB config file
You have to create a new file called `.env` in the devops folder with the following content:
```
# MySQL Configuration
MYSQL_ROOT_PASSWORD=S3cr3tP4ssw0rd
MYSQL_DATABASE=films_db
MYSQL_USER=user
MYSQL_PASSWORD=pass
MYSQL_PORT=3306
```

### API config file for DB connection
To configure the API connection with the DB, you have to create a new file called `application.properties` in the devops folder. This file should have the following content (configure it using the data from the `.env` file):
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

### Run the project
Then, you have to run the following command, placed in the devops folder:
```
docker compose up -d
```

### Stop the project
To stop the project, you have to run the following command, placed in the devops folder:
```
docker compose down
```

### API documentation
You can find the API documentation in the following link:
```
https://github.com/javibu13/sanvalero-2dam-firsteval/blob/main/api/apiFilm.yaml
```