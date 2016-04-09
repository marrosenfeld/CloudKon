package cloudKon.queue;

import cloudKon.task.Task;

public abstract class AbstractQueue implements Queue {

	public abstract void push(Task task);

	public abstract Task pop();
}
