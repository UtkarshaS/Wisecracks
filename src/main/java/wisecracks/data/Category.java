class Category{

    int categoryId;
    String categoryName;

    //Default contructor
    public Category(String categoryName){
        //retrieve CategoryId from DB for the given CategoryName
    }

    //Secondary Constructor
    public Category(int categoryId, String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}

class User{

    int userId; // Change to int in Db
    String firstName;
    String lastName;
    String addressLine1;
    String addressLine2;
    String city;
    String stateId;
    String profile;
    Date recentVisitedTime;

    //Default constructor
    public User(){
    }

    //Secondary constructor
    public User(){

    }
}

class Subscriber{

    int subscriberId; //change to int in Db //Do we even need this ?
    int userId;
    int forumId;
    Date subscriptionTime;

    //Default contructor
    public Subscriber(){

    }

    //Secondary constructor
    public Subscriber(){

    }
}