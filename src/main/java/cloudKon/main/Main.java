package cloudKon.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import cloudKon.client.Client;
import cloudKon.client.ClientThread;
import cloudKon.queue.LocalQueue;
import cloudKon.queue.Queue;
import cloudKon.worker.WorkerPool;
import cloudKon.worker.WorkerPoolThread;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		if (args[0].equalsIgnoreCase("client"))
			startClient(args);
		else if (args[0].equalsIgnoreCase("worker")) {
			startWorker(args);
		}
		System.out.println("End");
	}

	private static void startWorker(String[] args) {
		// String queueName = null;
		// Integer poolSize = null;
		// for (int i = 0; i < args.length; i++) {
		// switch (args[i]) {
		// case "-s":
		// queueName = args[i + 1];
		// break;
		// case "-t":
		// poolSize = Integer.valueOf(args[i + 1]);
		// break;
		// }
		// }
		//
		// if ((queueName == null || queueName.isEmpty()) || (poolSize == null))
		// {
		// throw new RuntimeException("Invalid arguments");
		// }
		//
		// if (queueName.equalsIgnoreCase("LOCAL")) {
		// sourceQueue = new LocalQueue();
		// resultQueue = new LocalQueue();
		// client = new Client(sourceQueue, resultQueue, filename);
		// client.doExperiment();
		// }

	}

	private static void startClient(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		String queueName = null;
		String filename = null;
		Client client = null;
		Queue sourceQueue = null;
		Queue resultQueue = null;
		Integer poolSize = null;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-s":
				queueName = args[i + 1];
				break;
			case "-w":
				filename = args[i + 1];
				break;
			case "-t":
				poolSize = Integer.valueOf(args[i + 1]);
				break;
			}

		}

		if ((queueName == null || queueName.isEmpty()) || (filename == null || filename.isEmpty()))

		{
			throw new RuntimeException("Invalid arguments");
		}

		if (queueName.equalsIgnoreCase("LOCAL"))

		{
			sourceQueue = new LocalQueue();
			resultQueue = new LocalQueue();
			// start local client thread
			client = new Client(sourceQueue, resultQueue, filename);
			ClientThread clientThread = new ClientThread(client);
			clientThread.start();
			// start local worker pool thread
			WorkerPool workerPool = new WorkerPool(poolSize, sourceQueue, resultQueue);
			WorkerPoolThread workerPoolThread = new WorkerPoolThread(workerPool);
			workerPoolThread.start();

			clientThread.join();
			System.out.println("Client joined");
			workerPoolThread.terminate();
			System.out.println("Worker terminated");
		}
	}

}