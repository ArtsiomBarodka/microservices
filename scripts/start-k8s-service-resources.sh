#!/bin/bash
function runK8sResource() {
  FOLDER=$1
  NAME=$2

  CURRENT=$(pwd)

  cd $FOLDER

  echo "Starting $NAME items..."
  kubectl apply -f .
  sleep 3s
  echo

  cd $CURRENT
}

cd ../k8s

echo "Starting service items"
runK8sResource service/api-gateway api-gateway
runK8sResource service/customer customer
runK8sResource service/order order
runK8sResource service/order-orchestrator order-orchestrator
runK8sResource service/product product
runK8sResource service/product-search product-search

kubectl get pods
sleep 10s
echo "Completed starting service items"