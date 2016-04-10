package cloudKon.worker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cloudKon.queue.Queue;

public class WorkerPool {

	private Integer poolSize;
	private Queue sourceQueue;
	private Queue resultQueue;
	private Set<Worker> workers;

	private ExecutorService executor;

	public WorkerPool(Integer poolSize, Queue sourceQueue, Queue resultQueue) {
		super();
		this.poolSize = poolSize;
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
		this.workers = new HashSet<Worker>();
	}

	public void doWork() throws InterruptedException {
		executor = Executors.newFixedThreadPool(this.poolSize);
		for (int i = 0; i < this.poolSize; i++) {
			Worker worker = new Worker(sourceQueue, resultQueue);
			executor.execute(worker);
			workers.add(worker);
		}
	}

	public void terminate() throws InterruptedException {
		for (Worker worker : workers) {
			worker.terminate();
		}

		executor.shutdown();
		executor.awaitTermination(24L, TimeUnit.HOURS);
		System.out.println("worker pool terminated");

	}
}
