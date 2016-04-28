package cloudKon.client;

import java.util.List;
import java.util.UUID;

import cloudKon.queue.Queue;
import cloudKon.task.Task;

public class ReceiverThread extends Thread {
	private List<UUID> taskIds;
	private Queue resultQueue;

	public ReceiverThread(List<UUID> taskIds, Queue resultQueue) {
		super();
		this.taskIds = taskIds;
		this.resultQueue = resultQueue;
	}

	@Override
	public void run() {
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
