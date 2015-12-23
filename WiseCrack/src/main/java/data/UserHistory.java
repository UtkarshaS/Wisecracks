package main.java.wisecracks.data;

import java.util.Date;

class UserHistory {
    int subscriber;
    int postId;
    Date lastSeen;

    public UserHistory(int subscriber, int postId, Date lastSeen) {
	this.subscriber = subscriber;
	this.postId = postId;
	this.lastSeen = lastSeen;
    }
}