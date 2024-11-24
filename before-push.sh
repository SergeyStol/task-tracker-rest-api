#! /bin/bash

echo
echo "***************************************************************************************"
echo "1. Smoke tests using docker-compose.yaml file and Dockerfile with layered jar --> start"
echo "***************************************************************************************"
echo
docker compose --profile app down --volumes
docker compose up -d app --build -V
docker compose exec postgres psql -U postgres -d tasktrackerrestapi -f ./scripts/cleandb.sql
docker compose up newman --exit-code-from newman -V
NEWMAN_EXIT_CODE=$?
docker compose --profile app down --volumes
echo
echo "****************************************************************************************"
echo "1. Smoke tests using docker-compose.yaml file and Dockerfile with layered jar --> finish"
echo "****************************************************************************************"
echo
if [ $NEWMAN_EXIT_CODE -eq 1 ]; then
  echo "Tests failed with exit code 1. Exiting."
  exit 1
fi

echo
echo "******************************************************************************************"
echo "2. Smoke tests using docker-compose-tests.yaml file and Dockerfile with uber jar --> start"
echo "******************************************************************************************"
echo
docker compose -f docker-compose-tests.yaml down --volumes
docker compose -f docker-compose-tests.yaml up --build --exit-code-from newman -V
NEWMAN_EXIT_CODE=$?
docker compose -f docker-compose-tests.yaml down --volumes
echo
echo "*******************************************************************************************"
echo "2. Smoke tests using docker-compose-tests.yaml file and Dockerfile with uber jar --> finish"
echo "*******************************************************************************************"
echo
if [ $NEWMAN_EXIT_CODE -eq 1 ]; then
  echo "Tests failed with exit code 1. Exiting."
  exit 1
fi

echo
echo "***************************************************"
echo "3. Unit and Integration tests using Maven --> start"
echo "***************************************************"
echo
./mvnw test
echo
echo "****************************************************"
echo "3. Unit and Integration tests using Maven --> finish"
echo "****************************************************"
echo

echo
echo "****************************************************"
echo "******************  SUCCESS  ***********************"
echo "****************************************************"
echo