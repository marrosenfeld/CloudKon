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
		Integer count = 0;
		while (!jobCompleted) {
			Task task = sourceQueue.pop();
			if (task != null) {
				// System.out.println("Pop Task: " + task);
				if (duplicateValidator.validate(task)) {
					count++;
					try {
						Thread.sleep(task.getSleepTime());
						task.setSuccess(true);
					} catch (Exception e) {
						e.printStackTrace();
						task.setSuccess(false);
					}
					resultQueue.push(task);
					if (count % 100 == 0) {
						System.out.println("Processed: " + count);
					}
				}
			} else {
				try {
					Thread.sleep(1000);
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
