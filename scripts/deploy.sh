#!/usr/bin/env bash

mvn clean package
echo 'Copy files...'
scp -i ~/.ssh/id_rsa \
  target/timetable-0.0.1-SNAPSHOT.jar \
  ra@192.168.100.182:/home/ra/
echo 'Restart server...'
ssh -tt -i ~/.ssh/id_rsa ra@192.168.100.182 <<EOF

pgrep java | xargs kill -9
nohup java -jar timetable-0.0.1-SNAPSHOT.jar &

EOF

echo 'Bye..'