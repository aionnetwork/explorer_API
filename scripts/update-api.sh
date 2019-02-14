#!/usr/bin/env bash

cd  ~/deployment/
rm ~/deployment/aion-api -R
cp ~/git/explorer_backend/aion-api ~/deployment/ -R
cd ~/deployment/aion-api
~/deployment/aion-api/mvnw package
java $AION_API_OPTS -jar target/aion-api-4.0.0-SNAPSHOT.jar
