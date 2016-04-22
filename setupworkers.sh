#!/bin/bash
filename='workers'
filelines=`cat $filename`
echo Start
for p in $filelines ; do
  	echo $p:
	scp -i prod.pem worker.sh ubuntu@$p:
	scp -i prod.pem target/cloudKon.jar ubuntu@$p:
done
