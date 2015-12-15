package main.java.wisecracks.data;

import java.util.Date;

public class Post {
    int postId;
    int forumId;
    String topic;
    int userId;
    String textBody;
    Date creationDate;

    Post(int postId, int forumId, String topic, int userId, String textBody, Date creationDate) {
	this.postId = postId;
	this.forumId = forumId;
	this.topic = topic;
	this.userId = userId;
	this.textBody = textBody;
	this.creationDate = creationDate;
    }

}
