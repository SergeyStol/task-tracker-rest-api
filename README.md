# task-tracker-rest-api


# Prerequisites
* Java JDK version 21 (https://jdk.java.net/java-se-ri/21)
* `git` (https://git-scm.org/)
* `docker` (https://docker.io/)
* `docker-compose` (https://docs.docker.com/compose/install/)
* curl

# Downloading
```shell
git clone https://github.com/SergeyStol/task-tracker-rest-api.git
cd task-tracker-rest-api
```

# Running for development
1. Run environment
   ```shell
   docker compose up -d postgres rabbitmq
   ```
2. Run application in your IDE
3. Run smoke tests
   ```shell
   docker compose up newman --no-deps
   ```
The last line you should see in your console - `newman-1 exited with code 0`.
It means, smoke tests had been passed successfully.
In the table above the last line you can watch some statistic.

Congratulations! The application is working correctly and against database and message broker.

Now, you can execute `docker-compose down newman` and start a development.
Have fun!

# Running for testing
Execute command:
```shell
docker compose app --build
```
It will start the application inside docker container with database and message broker.
To check that the application works correctly, see [Healthcheck](#Healthcheck)

# Healthcheck
```shell
curl loclahost:8080/actuator/health
```
You should see `{"status":"UP","groups":["liveness","readiness"]}`
if so, the application is working correctly.

# Testing

## Unit tests
To run unit tests:
```shell
./mvnw test
```

## Automatic Smoke Tests (newman)
Run smoke tests using docker compose:
```shell
docker compose -f docker-compose-tests.yaml up --build
```

# Clean database
To clean database execute
```shell
docker compose exec postgres psql -U postgres -d tasktrackerrestapi -f ./scripts/cleandb.sql
```

# Set up for production
1. generate key pair (public and private) for jwt token creation; for this proposes use script `./src/resources/rsa/generate.sh` or run manually
   ```shell
   openssl genpkey -algorithm RSA -out ./id_rsa -pkeyopt rsa_keygen_bits:2048
   openssl rsa -in ./id_rsa -pubout -out ./id_rsa.pub
   ```
   Put files `id_rsa` and `id_rsa.pub` inside folder `./src/resources/rsa`.
2. create file with a password from the database (the file should contain just the password) - `./db/password.txt`
   for ex.
   ```shell
   echo -n 12345 > ./db/password.txt

# Before push new commit
1. If you add a new entity or change one, check that you add changes to script `./scripts/cleandb.sql`.
2. run script `before-push.sh`