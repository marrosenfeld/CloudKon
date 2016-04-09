package cloudKon.queue;

import cloudKon.task.Task;

public interface Queue {

	void push(Task task);

	Task pop();

	Boolean isEmpty();
}
