package services;

public class SNSMobilePushHelper {
	
	private final String applicationArn = "arn:aws:sns:us-east-1:573875848159:app/GCM/WiseCracks_GCM_platform";
	SNSManager manager;

	public SNSMobilePushHelper(){
		manager = new SNSManager();
	}
	
	/**
	 * Create topic and subscribe the user's phone to topic
	 * @param topic length of topic < 256 characters
	 * @param endpointArn endpointArn of the user's device get it from user table in db.
	 * @return topicArn save this in forum table
	 */
	public String createTopicForForum(String topic, String endpointArn){
		topic = topic.replaceAll("[^a-zA-Z0-9]", "_");		
		System.out.println(topic);
		String topicArn = manager.createTopic(topic.substring(0, (topic.length()<256 ? topic.length() : 256)));
		subscribeEndpointToTopic(topicArn, endpointArn);
		
		return topicArn;
	}
	
	/**
	 * Publish notification of topic for forum
	 * @param topicArn: Get topicArn from forum table
	 * @param msg: json string with who commented e.g. json: {"commentby":"Sagar Shinde", "comment":"first few words of comment if possible otherwise leave it blank"}
	 */
	public void notifyAboutComment(String topicArn, String msg){
		manager.publishToTopic(topicArn, msg);
	}
	
	/**
	 * This will register the mobile device on SNS. Call it when you get registration id from mobile it will return EndpointArn Save it to Db in user table.
	 * @param registrationId 
	 * @param applicationArn
	 * @return endpointArn
	 */
	public String addRegIdtoSNS(String registrationId){
		return manager.createPlatformEndpoint("WiseCrack mobile Endpoint", registrationId, applicationArn).getEndpointArn();
	}
	
	/**
	 * When a user comments on forum subscribe that user to that topic.
	 * @param topicArn get it from forum table
	 * @param endpointArn get it from user table
	 */
	public void subscribeEndpointToTopic(String topicArn, String endpointArn){
		manager.SubscribeMobileToTopic(topicArn, endpointArn);
	}
}
