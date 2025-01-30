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