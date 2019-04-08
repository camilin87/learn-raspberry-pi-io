#!/usr/bin/env bash

RASPBERRY_PI_IP=""

if [[ $1 ]]; then
    RASPBERRY_PI_IP=$1
else
    echo "ERROR: missing raspberry pi IP"
    echo ""
    echo "Usage:"
    echo "------"
    echo "build-deploy-run.sh 192.168.1.2"
    echo "------"
    exit 1
fi


echo "Validating Java version"
java --version | grep "11.0"

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Incorrect Java Version Found"
    exit 2
fi


echo "Running the tests"
mvn clean test

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Tests Failed"
    exit 3
fi


echo "Packaging the app"
mvn clean package

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Package Failed"
    exit 4
fi


echo "Deploying the app"
scp target/learnrpi-*.jar pi@${RASPBERRY_PI_IP}:~/

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Deployment Failed"
    exit 5
fi


echo "Running the app"
ssh -t pi@${RASPBERRY_PI_IP} java -cp /home/pi/learnrpi-*.jar com.tddapps.learnrpi.Program

echo "Exit Code: $?"