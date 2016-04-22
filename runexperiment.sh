#!/bin/bash
numberoftasks=$1
sleeptime=$2
rm -f tasks
for i in $(eval echo {1..$numberoftasks});
        do
                echo 'sleep' $sleeptime >> tasks
        done
./client.sh
