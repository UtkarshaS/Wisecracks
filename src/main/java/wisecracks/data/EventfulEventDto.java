package main.java.wisecracks.data;

public class EventfulEventDto {
    private String title, description, startTime, venueName, venueAddress, city, country, latitude, longitude;
    private String categoryID, category;

    public EventfulEventDto(String title, String description, String startTime, String venueName, String venueAddress,
	    String city, String country, String latitude, String longitude) {

	this.title = title;
	this.description = description;
	this.startTime = startTime;
	this.venueName = venueName;
	this.venueAddress = venueAddress;
	this.city = city;
	this.country = country;
	this.latitude = latitude;
	this.longitude = longitude;
    }

    public EventfulEventDto(String categoryID, String category) {
	this.categoryID = categoryID;
	this.category = category;
    }

    public String getTitle() {
	return title;
    }

    public String getDescription() {
	return description;
    }

    public String getStartTime() {
	return startTime;
    }

    public String getVenueName() {
	return venueName;
    }

    public String getVenueAddress() {
	return venueAddress;
    }

    public String getCity() {
	return city;
    }

    public String getCountry() {
	return country;
    }

    public String getLatitude() {
	return latitude;
    }

    public String getLongitude() {
	return longitude;
    }

    public String getCategoryID() {
	return categoryID;
    }

    public String getCategory() {
	return category;
    }
}