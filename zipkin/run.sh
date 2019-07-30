#!/bin/bash
while ! `nc -z $CONFIGURATION_SERVER $CONFIGURATION_SERVER_PORT`;
    do sleep $SLEEP_TIME; echo "Waiting for $CONFIGURATION_SERVER server";done
java -jar -Dspring.cloud.config.uri="http://$CONFIGURATION_SERVER:$CONFIGURATION_SERVER_PORT" -Dspring.profiles.active=$DEFAULT_PROFILE zipkin-server.jar