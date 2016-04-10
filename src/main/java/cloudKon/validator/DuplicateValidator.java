package cloudKon.validator;

import cloudKon.task.Task;

public interface DuplicateValidator {

	/**
	 * @param task
	 * @return true if the task is validated
	 */
	Boolean validate(Task task);
}
