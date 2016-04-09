package cloudKon.worker;

public class WorkerPoolThread extends Thread {
	private WorkerPool workerPool;

	public WorkerPoolThread(WorkerPool workerPool) {
		super();
		this.workerPool = workerPool;
	}

	@Override
	public void run() {
		try {
			workerPool.doWork();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void terminate() {
		this.workerPool.terminate();
	}
}
