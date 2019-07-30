#!/bin/sh
docker rmi $(dokcer images -q | grep "^training")
docker rmi $(dokcer images -q | grep "<none>")

./mvnw clean package

docker build -t "training/configuration-server" configuration
docker build -t "training/discovery-server" discovery
docker build -t "training/gateway-server" discovery
docker build -t "training/zipkin-server" discovery