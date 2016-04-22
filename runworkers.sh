parallel-ssh -i -h workers -l ubuntu "nohup ./worker.sh < /dev/null >script.out 2>script.err &"
