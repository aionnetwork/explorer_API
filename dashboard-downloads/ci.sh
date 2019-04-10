#!/bin/bash
pid=$(lsof -i:8110 -t);

if [ -n "$pid" ]; then

    kill -TERM $pid
    kill -KILL $pid

fi

sudo su aion && source /home/aion/deployment/.env && /home/aion/deployment/dashboard-downloads/mvnw spring-boot:run >> /home/aion/deployment/dashboard-downloads/logs/dashboard-downloads.log &