#!/usr/bin/env bash

newman run "task-tracker-rest-api tests.postman_collection.json" -e http-localhost-8080.postman_environment.json