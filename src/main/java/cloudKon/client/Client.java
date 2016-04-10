package cloudKon.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		Integer countTasks = this.pushTasks();
		this.receiveResults(countTasks);
	}

	/**
	 * @param countTasks
	 * @throws InterruptedException
	 *             Receive the ack for all tasks from the resultqueue.
	 */
	private void receiveResults(Integer countTasks) throws InterruptedException {
		Integer completedTasks = 0;
		while (completedTasks < countTasks) {
			Task task = this.resultQueue.pop();
			if (task != null) {
				completedTasks++;
				System.out.println("Received completed task: " + task.getId());
			} else {
				Thread.sleep(10);
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
	private Integer pushTasks() throws FileNotFoundException, IOException {
		Integer countTasks = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				Long sleepTime = Long.valueOf(line.split(" ")[1]);
				Task task = new Task(UUID.randomUUID(), sleepTime);
				sourceQueue.push(task);
				countTasks++;
				System.out.println("Push task: " + task);
			}
		}
		System.out.println("Push tasks: " + countTasks);
		return countTasks;
	}

}
