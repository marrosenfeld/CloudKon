#!/bin/bash
p=$(head -n 1 client)  	
echo $p:
scp -i prod.pem client.sh ubuntu@$p:
scp -i prod.pem target/cloudKon.jar ubuntu@$p:
scp -i prod.pem runexperiment.sh ubuntu@$p:
scp -i prod.pem runlocal.sh ubuntu@$p:
