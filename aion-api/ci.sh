#!/bin/bash
pid=$(lsof -i:8080 -sTCP:LISTEN -t);
API_DIR=/home/aion/deployment/api2

if [ -n "$pid" ]; then

    kill -TERM $pid
    kill -KILL $pid

fi

#byobu send-keys -t 'API2' "source ../.env && ./mvnw spring-boot:run   -Dserver.port=8087 -DDATADOG_ENABLE=true -Dcom.sun.management.jmxremote.port=7092 -Dcom.sun.management.jmxremote.rmit=7092 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false" 'C-m'
#source /home/aion/deployment/.env && $API_DIR/mvnw spring-boot:run >> $API_DIR/logs/api.log &


#API_DIR=/home/aion/deployment/api2
sudo su aion && source /home/aion/deployment/.env && /home/aion/deployment/api2/mvnw spring-boot:run &>> /home/aion/deployment/api2/logs/api.log &
