
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

public class SNSManager {
	
	private AWSCredentials credentials;
	private AmazonSNSClient snsClient;

	public SNSManager() {
		// TODO Auto-generated constructor stub
		
		try {
			credentials = new PropertiesCredentials(
					SNSManager.class.getResourceAsStream("AwsCredentials.properties"));
			//create a new SNS client and set endpoint
			snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());		                           
			snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private CreateTopicResult createTopic(String topic){
		
		//create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topic);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//print TopicArn
		System.out.println(createTopicResult);
		//get request id for CreateTopicRequest from SNS metadata		
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		
		return createTopicResult;
		
	}
	
	private void SubscribeToTopic(String topicArn){
		
		//subscribe to an SNS topic via EMAIL
		
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", /*email*/" ");
		snsClient.subscribe(subRequest);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("Check your email and confirm subscription.");
		
		//subscribe to an SNS topic via MobileNotifications
		
		SubscribeRequest subRequestApp = new SubscribeRequest(topicArn, "application", "/*AppARN*/");
		snsClient.subscribe(subRequestApp);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestApp));
		System.out.println("Check your email and confirm subscription.");
		
		//subscribe to an SNS topic via HttpEndpoint
		
		SubscribeRequest subRequestHttp = new SubscribeRequest(topicArn, "http", "/*httpendpoint*/");
		snsClient.subscribe(subRequestHttp);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestHttp));
		System.out.println("Check your email and confirm subscription.");
		
		//subscribe to an SNS topic via HttpEndpoint
		
		SubscribeRequest subRequestSQS = new SubscribeRequest(topicArn, "sqs", "/*sqsArn*/");
		snsClient.subscribe(subRequestSQS);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestSQS));
		System.out.println("Check your email and confirm subscription.");
		
	}
	
	private void deleteTopic(String topicArn){
		//delete an SNS topic
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		//get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
	}
	
	private void publishToTopic(String topicArn){
		//publish to an SNS topic
		String msg = "My text published to SNS topic with email endpoint";
		PublishRequest publishRequest = new PublishRequest(topicArn, msg); // TODO: json msg for different protocols see documentation.
		PublishResult publishResult = snsClient.publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}
}
