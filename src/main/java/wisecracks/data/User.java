class User{

    int userId; //Auto-increment retrieval from DB
    String firstName;
    String lastName;
    String addressLine1 = null;
    String addressLine2 = null;
    String city = null;
    String country = null;
    String profile = null;
    Date recentVisitedTime;

    //Primary constructor
    public User(userId, firstName, lastName, recentVisitedTime){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.recentVisitedTime = recentVisitedTime;
    }

    //Secondary constructor
    public User(firstName, lastName, addressLine1, addressLine2, city, stateId, profile, recentVisitedtime){
        this.userId = urerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.stateId = stateId;
        this.profile = profile;
        this.recentVisitedTime = recentVisitedTime;
    }
}