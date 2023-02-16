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

runK8sResource shared/ shared
kubectl get persistentvolume
kubectl get persistentvolumeclaim
echo "Completed starting shared items"

echo "Starting bootstrap items"
runK8sResource bootstrap/kafka kafka
runK8sResource bootstrap/keycloak keycloak
runK8sResource bootstrap/mongo mongo
runK8sResource bootstrap/mysql-customer mysql-customer
runK8sResource bootstrap/mysql-order mysql-order
runK8sResource bootstrap/zipkin zipkin
runK8sResource bootstrap/elk elk

kubectl get pods
sleep 10s
echo "Completed starting bootstrap items"