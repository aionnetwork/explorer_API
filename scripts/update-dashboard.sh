#!/usr/bin/env bash

cd  ~/deployment/
rm ~/deployment/dashboard -R
cp ~/git/explorer_backend/dashboard ~/deployment/ -R
cd ~/deployment/dashboard
~/deployment/dashboard/mvnw package
source ~/deployment/.env
~/deployment/dashboard/mvnw spring-boot:run