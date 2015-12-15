package main.java.wisecracks.data;

import java.util.Date;

class Subscriber {

    int subscriberId;
    int forumId;
    Date subscriptionTime;

    public Subscriber(int subscriberId, int userId, int forumId, Date subscriptionTime) {
	this.subscriberId = subscriberId;
	this.forumId = forumId;
	this.subscriptionTime = subscriptionTime;
    }
}