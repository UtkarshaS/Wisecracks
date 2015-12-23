package data;
public class User {

    final String userId;
    final String firstName;
    final String lastName;
    final String addressLine1;
    final String addressLine2;
    final String city;
    final String country;
    final String profile;

    public User(String userId, String firstName, String lastName, String addressLine1, String addressLine2,
	    String city, String country, String profile) {
	this.userId = userId;
	this.firstName = firstName;
	this.lastName = lastName;
	this.addressLine1 = addressLine1;
	this.addressLine2 = addressLine1;
	this.city = city;
	this.country = country;
	this.profile = profile;

    }

}
