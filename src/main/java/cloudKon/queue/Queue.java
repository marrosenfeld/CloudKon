package cloudKon.queue;

import cloudKon.task.Task;

/**
 * @author mrosenfeld Interface for queue
 */
public interface Queue {

	void push(Task task);

	Task pop();

}
