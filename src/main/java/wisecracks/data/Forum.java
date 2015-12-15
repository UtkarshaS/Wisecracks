package main.java.wisecracks.data;

import java.util.Date;

public class Forum {

    int forumId;
    double latitude;
    double longitude;
    String title;
    int userId;
    String description;
    int categoryId;
    boolean isActive = true; // We include this when required in work flow.
    Date createdDate;

    public Forum(int forumId, double latitude, double longitude, String title, int userId, String description,
            int categoryId, Date createdDate) {
        this.forumId = forumId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.userId = userId;
        this.description = description;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
    }

}
