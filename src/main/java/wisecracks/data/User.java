package main.java.wisecracks.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Connection_class;
import java.sql.Connection;

class User {

    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String addressLine1;
    private final String addressLine2;
    private final String city;
    private final String country;
    private final String profile;

    // Primary constructor
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

    public void createUserProfile() throws Exception {
        PreparedStatement statement = null;
        try {
            String insertStatement = "Insert into user(userId,firstName,lastName,addressLine1,addressLine2,city,country, profile, recentvisitedTime) "
                    + "values (?,?,?,?,?,?,?,?,now())";
            Connection connection = Connection_class.createConnection();
            statement = connection.prepareStatement(insertStatement);
            statement.setString(1, userId);
            statement.setString(2, firstName);
            statement.setString(4, lastName);
            statement.setString(2, addressLine1);
            statement.setString(2, addressLine2);
            statement.setString(2, city);
            statement.setString(2, country);
            statement.setString(2, profile);
            statement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public User getUserProfile(String userId) throws Exception {
        PreparedStatement statement = null;
        User newUser = null;
        ResultSet rs = null;
        try {
            String selectStatement = "Select firstName,lastName,addressLine1,addressLine2,city,country, profile, recentvisitedTime from user "
                    + " where userId = " + userId;

            Connection connection = Connection_class.createConnection();
            statement = connection.prepareStatement(selectStatement);
            rs = statement.executeQuery(selectStatement);
            while (rs.next()) {
                newUser = new User(rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("addressLine1"), rs.getString("addressLine2"), rs.getString("city"),rs.getString("country"),rs.getString("profile"),rs.getString("recentvisitedTime") );
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return newUser;
    }

}
