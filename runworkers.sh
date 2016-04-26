parallel-ssh -i -h workers -l ubuntu "nohup ./worker.sh $1 $2 < /dev/null >script.out 2>script.err &" 
