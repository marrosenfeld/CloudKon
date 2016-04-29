package cloudKon.queue;

import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import cloudKon.task.Task;

/**
 * @author mrosenfeld SQS queue implementation
 */
public class SQSQueue extends AbstractQueue {

	private AmazonSQS sqs;
	private String queueUrl;

	/**
	 * @param profile
	 * @param queueName
	 *            Gets the queue url for the queue name
	 */
	public SQSQueue(String profile, String queueName) {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (/home/mrosenfeld/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider(profile).getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/home/mrosenfeld/.aws/credentials), and is in valid format.", e);
		}
		sqs = new AmazonSQSClient(credentials);
		sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
		ListQueuesResult queues = sqs.listQueues();
		this.queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	}

	/*
	 * Push task as sqs message
	 */
	@Override
	public void push(Task task) {
		sqs.sendMessage(new SendMessageRequest(queueUrl, task.getId().toString().concat(",")
				.concat(task.getSleepTime().toString()).concat(",").concat(task.getSuccess().toString())));
	}

	/*
	 * pop task from sqs. Returns null if no message available
	 */
	@Override
	public Task pop() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.queueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
		if (receiveMessageResult.getMessages().size() > 0) {
			Message message = receiveMessageResult.getMessages().get(0);
			// delete message from sqs
			sqs.deleteMessage(new DeleteMessageRequest(this.queueUrl, message.getReceiptHandle()));
			Task task = new Task(UUID.fromString(message.getBody().split(",")[0]),
					Long.parseLong(message.getBody().split(",")[1]),
					Boolean.parseBoolean(message.getBody().split(",")[2]));

			return task;
		} else {
			return null;
		}

	}

}
