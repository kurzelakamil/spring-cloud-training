#!/bin/sh
docker rmi $(docker images | grep "^training")
docker rmi $(docker images | grep "<none>")

./mvnw clean package

docker build -t "training/configuration-server" configuration
docker build -t "training/discovery-server" discovery
docker build -t "training/gateway-server" gateway
docker build -t "training/zipkin-server" zipkin
docker build -t "training/departments" departments
docker build -t "training/users" users