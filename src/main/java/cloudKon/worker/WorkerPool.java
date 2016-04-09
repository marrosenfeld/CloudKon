package cloudKon.worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cloudKon.queue.Queue;
import cloudKon.task.Task;

public class WorkerPool {

	Integer poolSize;
	Queue sourceQueue;
	Queue resultQueue;
	private boolean jobCompleted = false;

	public WorkerPool(Integer poolSize, Queue sourceQueue, Queue resultQueue) {
		super();
		this.poolSize = poolSize;
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
	}

	public void doWork() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(this.poolSize);
		while (!jobCompleted) {
			Task task = sourceQueue.pop();
			if (task != null) {
				System.out.println("Pop Task: " + task);
				Runnable worker = new Worker(sourceQueue, resultQueue, task);
				executor.execute(worker);
			} else {
				Thread.sleep(10);
			}
		}
		System.out.println("worker pool terminated");
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}

	public void terminate() {
		this.jobCompleted = true;
	}
}
