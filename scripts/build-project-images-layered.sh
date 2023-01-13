#!/bin/bash

function unpack() {
  FOLDER=$1
  NAME=$2

  CURRENT=$(pwd)

  cd $FOLDER/build/libs
  java -jar -Djarmode=layertools ${NAME}.jar extract

  cd $CURRENT
}

function build() {
  FOLDER=$1
  NAME=$2

  docker build -f ./docker/Dockerfile \
    --build-arg JAR_FOLDER=${FOLDER}/build/libs \
    -t ${NAME}:latest \
    -t ${NAME}:layered .
}

cd ..

echo "Building JAR files"
gradle clean bootJar

echo "Unpacking JARs"
unpack config-server config-server
unpack discovery-server discovery-server
unpack product product
unpack order order
unpack customer customer
unpack api-gateway api-gateway
unpack order-orchestrator order-orchestrator
unpack product-search product-search

echo "Building Docker image"
build config-server application/config-server
build discovery-server application/discovery-server
build product application/product
build order application/order
build customer application/customer
build api-gateway application/api-gateway
build order-orchestrator application/order-orchestrator
build product-search application/product-search