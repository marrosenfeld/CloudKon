package cloudKon.worker;

import cloudKon.queue.Queue;
import cloudKon.task.Task;

public class Worker implements Runnable {
	// private Task task;
	private Queue resultQueue;
	private Queue sourceQueue;
	private Task task;

	public Worker(Queue sourceQueue, Queue resultQueue, Task task) {
		super();
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
		this.task = task;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(task.getSleepTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resultQueue.push(task);

	}

}
