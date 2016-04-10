package cloudKon.worker;

import cloudKon.queue.Queue;
import cloudKon.task.Task;

public class Worker implements Runnable {
	// private Task task;
	private Queue resultQueue;
	private Queue sourceQueue;
	private Boolean jobCompleted;

	public Worker(Queue sourceQueue, Queue resultQueue) {
		super();
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
		this.jobCompleted = false;
	}

	@Override
	public void run() {
		while (!jobCompleted) {
			Task task = sourceQueue.pop();
			if (task != null) {
				System.out.println("Pop Task: " + task);
				try {
					Thread.sleep(task.getSleepTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultQueue.push(task);
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Worker Terminated");

	}

	public void terminate() {
		this.jobCompleted = true;

	}

}
