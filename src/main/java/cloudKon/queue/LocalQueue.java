package cloudKon.queue;

import java.util.LinkedList;

import cloudKon.task.Task;

/**
 * @author mrosenfeld In memory queue implementation
 */
public class LocalQueue extends AbstractQueue {
	private java.util.Queue<Task> queue;

	public LocalQueue() {
		this.queue = new LinkedList<Task>();
	}

	@Override
	public synchronized void push(Task task) {
		this.queue.add(task);
	}

	@Override
	public synchronized Task pop() {
		return this.queue.poll();
	}

}
