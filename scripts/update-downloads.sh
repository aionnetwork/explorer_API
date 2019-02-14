#!/usr/bin/env bash

cd  ~/deployment/
rm ~/deployment/dashboard-downloads -R
cp ~/git/explorer_backend/dashboard-downloads ~/deployment/ -R
cd ~/deployment/dashboard-downloads
~/deployment/dashboard-downloads/mvnw package
java $AION_EXPORTER_OPTS -jar target/aion-exporter-1.0.0-SNAPSHOT.jar
