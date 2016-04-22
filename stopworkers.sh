parallel-ssh -i -h workers -l ubuntu "nohup pkill -f cloudKon < /dev/null >script.out 2>script.err &"
