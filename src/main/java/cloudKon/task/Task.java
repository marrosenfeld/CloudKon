package cloudKon.task;

import java.util.UUID;

public class Task {
	UUID id;
	Long sleepTime;

	public Task(UUID id, Long sleepTime) {
		super();
		this.id = id;
		this.sleepTime = sleepTime;
	}

	public UUID getId() {
		return id;
	}

	public Long getSleepTime() {
		return sleepTime;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", sleepTime=" + sleepTime + "]";
	}

}
