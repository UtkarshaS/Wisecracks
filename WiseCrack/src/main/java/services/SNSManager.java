package services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class SNSManager {

	private AmazonSNSClient snsClient;


	public SNSManager() {
		// TODO Auto-generated constructor stub

		AWSCredentials credentials = null;
		/*
		 * try { credentials = new
		 * ProfileCredentialsProvider("sagar_s").getCredentials(); } catch
		 * (Exception e) { throw new AmazonClientException(
		 * "Cannot load the credentials from the credential profiles file. " +
		 * "Please make sure that your credentials file is at the correct " +
		 * "location (C:\\Users\\_Username_\\.aws\\credentials), and is in valid format."
		 * , e); }
		 */

		try {
			credentials = new PropertiesCredentials(
					SNSManager.class.getResourceAsStream("AwsCredentials.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// initialize SNSClient
		snsClient = new AmazonSNSClient(credentials);
		Region region = Region.getRegion(Regions.US_EAST_1);
		snsClient.setRegion(region);
	}

	/**
	 * Create Topic on SNSClient
	 * 
	 * @param topic
	 * @return Topic ARN of the created SNS Topic
	 */
	public String createTopic(String topic) {

		// create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topic);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		// print TopicArn
		System.out.println(createTopicResult.getTopicArn());
		// get request id for CreateTopicRequest from SNS metadata
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));

		return createTopicResult.getTopicArn();

	}

	/**
	 * Subscribe to topic on SNS
	 * 
	 * @param topicArn
	 */
	public void SubscribeToTopic(String topicArn) {

		// subscribe to an SNS topic via EMAIL

		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", /* email */" ");
		snsClient.subscribe(subRequest);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
//		System.out.println("Check your email and confirm subscription.");

		// subscribe to an SNS topic via MobileNotifications

		SubscribeRequest subRequestApp = new SubscribeRequest(topicArn, "application", "/*AppARN*/");

		snsClient.subscribe(subRequestApp);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestApp));
		System.out.println("Check your email and confirm subscription.");

		// subscribe to an SNS topic via HttpEndpoint

		SubscribeRequest subRequestHttp = new SubscribeRequest(topicArn, "http", "/*httpendpoint*/");
		snsClient.subscribe(subRequestHttp);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestHttp));
		System.out.println("Check your email and confirm subscription.");

		// subscribe to an SNS topic via HttpEndpoint

		SubscribeRequest subRequestSQS = new SubscribeRequest(topicArn, "sqs", "/*sqsArn*/");
		snsClient.subscribe(subRequestSQS);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestSQS));
		System.out.println("Check your email and confirm subscription.");

	}

	/**
	 * Publish to SNS Topic
	 * 
	 * @param msg
	 */
	public void publishToTopic(String topicArn, String msg) {
		// publish to an SNS topic

		PublishRequest publishRequest = new PublishRequest(topicArn, msg); //
		PublishResult publishResult = snsClient.publish(publishRequest);
		// print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}

	/**
	 * Delete Topic
	 * 
	 * @param topicArn
	 */
	public void deleteTopic(String topicArn) {
		// delete an SNS topic
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		// get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
	}

	// ############################ FOR MOBILE PUSH
	// ###################################

	
	/***
	 * Creates Platform application for GCM on SNS  //No need to use it in our project we will create one from backend
	 * @param applicationName
	 * @param principal
	 * @param credential
	 * @return
	 */
	public CreatePlatformApplicationResult createPlatformApplication(String applicationName, String principal,
			String credential) {

		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(Platform.GCM.name());
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	/**
	 * Creates platform endpoint for each android device
	 * it will return plantformArn which need to be stored in db in user table.
	 * @param customData
	 * @param platformToken
	 * @param applicationArn
	 * @return
	 */
	public CreatePlatformEndpointResult createPlatformEndpoint(String customData, String platformToken,
			String applicationArn) {

		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		String token = platformToken;
		String userId = null;

		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	/**
	 * Deletes Platform application
	 * @param applicationArn
	 */
	public void deletePlatformApplication(String applicationArn) {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	/**
	 * Subscribes mobile device to SNS tipic
	 * @param topicArn
	 * @param endpointArn
	 */
	public void SubscribeMobileToTopic(String topicArn, String endpointArn) {
		SubscribeRequest subRequestApp = new SubscribeRequest(topicArn, "application", endpointArn);

		snsClient.subscribe(subRequestApp);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequestApp));
//		System.out.println("Check your email and confirm subscription.");
	}
	
	public  enum Platform {
		// Apple Push Notification Service
		APNS,
		// Sandbox version of Apple Push Notification Service
		APNS_SANDBOX,
		// Amazon Device Messaging
		ADM,
		// Google Cloud Messaging
		GCM,
		// Baidu CloudMessaging Service
		BAIDU,
		// Windows Notification Service
		WNS,
		// Microsoft Push Notificaion Service
		MPNS;
	}

}
