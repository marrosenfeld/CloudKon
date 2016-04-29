package cloudKon.task;

import java.util.UUID;

/**
 * @author mrosenfeld Task to be distributed
 */
public class Task {
	// uniqeu identification of the task
	UUID id;
	Long sleepTime;
	Boolean success;

	public Task(UUID id, Long sleepTime) {
		super();
		this.id = id;
		this.sleepTime = sleepTime;
		this.success = false;
	}

	public Task(UUID id, Long sleepTime, Boolean success) {
		super();
		this.id = id;
		this.sleepTime = sleepTime;
		this.success = success;
	}

	public UUID getId() {
		return id;
	}

	public Long getSleepTime() {
		return sleepTime;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", sleepTime=" + sleepTime + "]";
	}

}
