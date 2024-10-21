# task-tracker-rest-api


# Getting Started
## Prerequisites
* Java JDK version 23 (https://jdk.java.net/23/)
* `git` (https://git-scm.org/)
* `docker` (https://docker.io/)
* `docker-compose` (https://docs.docker.com/compose/install/)
* curl

## Prerequisites for 

## Installing
```shell
git clone git@github.com:sstol/task-tracker-rest-api
cd task-tracker-rest-api
./mvnw clean package
```

# Running

## Set up
Before running the application, there are a few things you need to set up first:
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
   ```

## Running locally

### using maven
To run the service from the development machine against already started PostgreSQL, use
```
./mvnw spring-boot:run
```

### using docker-compose
To run the service against a locally-running version of PostgreSQL using docker-compose, execute the following command:
```shell
docker-compose up app
```

This will launch PostgreSQL locally on port 5432 first and after will run task-tracker-rest-api service listening on `http://localhost:8080`.


## Healthcheck

Check the service is healthy:
```shell
curl http://localhost:8080/users/me
```

# Testing

## Unit tests
To run unit tests:
```shell
./mvnw test
```

## Automatic Smoke Tests (newman)
You should clean database each time before run integration tests.
```shell
dc exec postgres psql -U postgres -d tasktrackerrestapi -f ./scripts/cleandb.sql
```
Run integration tests using docker compose:
```shell
docker-compose up newman
```
or using docker directly
```shell
docker run --mount type=bind,src="$(pwd)"/postman,dst=/etc/newman --network host postman/newman run "task-tracker-rest-api tests.postman_collection.json" -e http-host.docker.internal-8080.postman_environment.json
```



