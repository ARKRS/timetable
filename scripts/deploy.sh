#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/timetable-1.0.1.jar \
    ra@ex4140:/C:/users/ra/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa ra@ex4140 << EOF

pgrep java |  | xargs kill -9
nohup java -jar timetable-1.0.1.jar > log.txt &


EOF



