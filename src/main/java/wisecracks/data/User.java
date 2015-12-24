package main.java.wisecracks.data;

public class User {

     String userId;
     String firstName;
     String lastName;
     String addressLine1;
     String addressLine2;
     String city;
     String country;
     String profile;
    String registrationEndPoint;
    public User(String userId, String firstName, String lastName, String addressLine1, String addressLine2,
	    String city, String country, String profile, String registrationEndPoint) {
	this.userId = userId;
	this.firstName = firstName;
	this.lastName = lastName;
	this.addressLine1 = addressLine1;
	this.addressLine2 = addressLine1;
	this.city = city;
	this.country = country;
	this.profile = profile;
	this.registrationEndPoint= registrationEndPoint;
    }

}
