package main.java.wisecracks.data;

import java.util.Date;
import java.util.List;

public class Forum {

    int forumId;
    double latitude;
    double longitude;
    double radius;
    String title;
    String userId;
    String topicARN;
    String description;
    int categoryId;
    boolean isActive = true; // We include this when required in work flow.
    Date createdDate;
    List<Post> relatedPosts; //initialized during getForum

    public Forum(int forumId, double latitude, double longitude,double radius,String title,String userId,String topicARN, String description,
            int categoryId, Date createdDate) {
        this.forumId = forumId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.title = title;
        this.userId = userId;
        this.topicARN = topicARN;
        this.description = description;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
    }

}
