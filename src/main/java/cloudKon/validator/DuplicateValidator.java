package cloudKon.validator;

import cloudKon.task.Task;

/**
 * @author mrosenfeld Interface of duplicate validators
 */
public interface DuplicateValidator {

	/**
	 * @param task
	 * @return true if the task is validated
	 */
	Boolean validate(Task task);
}
