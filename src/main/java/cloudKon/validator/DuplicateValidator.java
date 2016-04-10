package cloudKon.validator;

import cloudKon.task.Task;

public interface DuplicateValidator {

	/**
	 * @param task
	 * @return true if the task is duplicate
	 */
	Boolean isDuplicate(Task task);
}
