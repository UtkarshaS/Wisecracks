package data;

import java.util.Date;
import java.util.List;

public class Forum {

    int forumId = 0;
    double latitude;
    double longitude;
    double radius;
    String title;
    String userId;
    String topicARN ;
    String description="";
    int categoryId;
    boolean isActive = true; // We include this when required in work flow.
    Date createdDate;
    List<Post> relatedPosts; //initialized during getForum

//    public Forum(String userId, String title, double radius, double latitude, double longitude, int categoryId, String created) throws ParseException {
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.radius = radius;
//        this.title = title;
//        this.userId = userId;
//        this.categoryId = categoryId;
//        //Convert string to date
//        DateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
//        this.createdDate = tempDate.parse(created);
//    }

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
