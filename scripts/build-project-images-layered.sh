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

  docker push ${NAME}:layered
}

cd ..

echo "Building JAR files"
gradle clean bootJar

echo "Unpacking JARs"
unpack product product
unpack order order
unpack customer customer
unpack api-gateway api-gateway
unpack order-orchestrator order-orchestrator
unpack product-search product-search

echo "Building Docker image"
build product artsiombarodka/product
build order artsiombarodka/order
build customer artsiombarodka/customer
build api-gateway artsiombarodka/api-gateway
build order-orchestrator artsiombarodka/order-orchestrator
build product-search artsiombarodka/product-search