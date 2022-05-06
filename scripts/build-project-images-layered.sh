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
unpack customer customer
unpack order order

echo "Building Docker image"
build config-server application/config-server
build discovery-server application/discovery-server
build customer application/customer
build order application/order