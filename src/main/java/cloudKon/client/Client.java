package cloudKon.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cloudKon.queue.Queue;
import cloudKon.task.Task;

/**
 * @author mrosenfeld Client side of cloudKon
 */
public class Client {
	private Queue sourceQueue;
	private Queue resultQueue;
	private String filename;

	public Client(Queue sourceQueue, Queue resultQueue, String filename) {
		super();
		this.sourceQueue = sourceQueue;
		this.resultQueue = resultQueue;
		this.filename = filename;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 *             push the tasks and wait to receive the ack's
	 */
	public void doExperiment() throws FileNotFoundException, IOException, InterruptedException {
		final long startTime = System.currentTimeMillis();
		// push tasks
		List<UUID> taskIds = this.pushTasks();
		// receive results
		this.receiveResults(taskIds);
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime));
	}

	/**
	 * @param countTasks
	 * @throws InterruptedException
	 *             Receive the ack for all tasks from the resultqueue.
	 */
	private void receiveResults(List<UUID> taskIdsList) throws InterruptedException {
		Integer count = 0;
		List<UUID> taskIds = Collections.synchronizedList(taskIdsList);
		ExecutorService executor = Executors.newFixedThreadPool(8);
		for (int i = 0; i < 8; i++) {
			ReceiverThread receiver = new ReceiverThread(taskIds, resultQueue);
			executor.execute(receiver);

		}

		executor.shutdown();
		executor.awaitTermination(24L, TimeUnit.HOURS);
		System.out.println("All tasks completed");

	}

	/**
	 * @return the number of tasks pushed
	 * @throws FileNotFoundException
	 * @throws IOException
	 *             Push tasks to the source queue
	 */
	private List<UUID> pushTasks() throws FileNotFoundException, IOException {

		List<UUID> taskIds = new ArrayList<UUID>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			// get one task per line
			while ((line = br.readLine()) != null) {
				Long sleepTime = Long.valueOf(line.split(" ")[1]);
				Task task = new Task(UUID.randomUUID(), sleepTime);
				// push task to queue
				sourceQueue.push(task);
				taskIds.add(task.getId());
			}
		}
		System.out.println("Push tasks: " + taskIds.size());
		return taskIds;
	}

}
