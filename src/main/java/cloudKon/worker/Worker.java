package cloudKon.worker;

import cloudKon.queue.Queue;
import cloudKon.task.Task;
import cloudKon.validator.DuplicateValidator;

public class Worker implements Runnable {
	// private Task task;
	private Queue resultQueue;
	private Queue sourceQueue;
	private Boolean jobCompleted;
	private DuplicateValidator duplicateValidator;

	public Worker(Queue sourceQueue, Queue resultQueue, DuplicateValidator duplicateValidator) {
		super();
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
		this.jobCompleted = false;
		this.duplicateValidator = duplicateValidator;
	}

	@Override
	public void run() {
		while (!jobCompleted) {
			Task task = sourceQueue.pop();
			if (task != null) {
				System.out.println("Pop Task: " + task);
				if (duplicateValidator.validate(task)) {
					try {
						Thread.sleep(task.getSleepTime());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					resultQueue.push(task);
				}
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
