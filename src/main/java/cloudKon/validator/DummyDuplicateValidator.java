package cloudKon.validator;

import cloudKon.task.Task;

/**
 * @author mrosenfeld Dummy validator which returns always true. To be used in
 *         local implementation
 */
public class DummyDuplicateValidator implements DuplicateValidator {

	@Override
	public Boolean isDuplicate(Task task) {
		return true;
	}

}
