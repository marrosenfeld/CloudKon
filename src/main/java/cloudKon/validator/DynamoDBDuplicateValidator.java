package cloudKon.validator;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import cloudKon.task.Task;

/**
 * @author mrosenfeld Uses DynamoDB to detect duplicate tasks
 */
public class DynamoDBDuplicateValidator implements DuplicateValidator {
	private AmazonDynamoDBClient dynamoDB;
	private String tableName;

	public DynamoDBDuplicateValidator(String tableName) {

		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/home/mrosenfeld/.aws/credentials), and is in valid format.", e);
		}
		dynamoDB = new AmazonDynamoDBClient(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);
		this.tableName = tableName;
	}

	@Override
	public Boolean validate(Task task) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("taskid", new AttributeValue(task.getId().toString()));
		item.put("sleepTime", new AttributeValue(task.getSleepTime().toString()));

		Map<String, ExpectedAttributeValue> expected = new HashMap<String, ExpectedAttributeValue>();
		expected.put("taskid", new ExpectedAttributeValue().withExists(false));

		PutItemRequest putItemRequest = new PutItemRequest(this.tableName, item);
		putItemRequest.withExpected(expected);

		try {
			PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
			System.out.println("Result: " + putItemResult);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

}
