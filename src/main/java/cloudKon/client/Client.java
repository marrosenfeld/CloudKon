package cloudKon.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

		List<UUID> taskIds = this.pushTasks();

		this.receiveResults(taskIds);
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime));
	}

	/**
	 * @param countTasks
	 * @throws InterruptedException
	 *             Receive the ack for all tasks from the resultqueue.
	 */
	private void receiveResults(List<UUID> taskIds) throws InterruptedException {
		Integer count = 0;
		while (!taskIds.isEmpty()) {
			Task task = this.resultQueue.pop();
			if (task != null) {
				if (!task.getSuccess()) {
					System.out.println("Error: failed task: " + task.getId());
				}
				if (!taskIds.remove(task.getId())) {
					System.out.println("Warning: receive id not expected");
				}
				count++;
				if (count % 100 == 0) {
					System.out.println("Already received: " + count);
				}
			} else {
				Thread.sleep(1000);
			}
		}
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
			while ((line = br.readLine()) != null) {
				Long sleepTime = Long.valueOf(line.split(" ")[1]);
				Task task = new Task(UUID.randomUUID(), sleepTime);
				sourceQueue.push(task);
				taskIds.add(task.getId());
				// System.out.println("Push task: " + task);
			}
		}
		System.out.println("Push tasks: " + taskIds.size());
		return taskIds;
	}

}
