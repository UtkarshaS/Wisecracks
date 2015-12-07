package main.java.wisecracks.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.Connection_class;

class Forum {
    
    private final double latitude;
    private final double longitude;
    private final int radius;
    private final String title;
    private final int creatorId;
    private final String description;
    private final int categoryId;
    private final boolean isActive;

    // Primary constructor
    public Forum(double latitude, double longitude, int radius, String title, int creatorId,String description, int categoryId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.title = title;
        this.creatorId = creatorId;
        this.description = description;
        this.categoryId = categoryId;
        this.isActive = true;
    }
    public boolean checkIfForumExists(String forumName) throws Exception {
        PreparedStatement statement = null;
        try {
            String selectStatement = "Select * from forum where title = " + forumName;
            Connection connection = Connection_class.createConnection();
            statement = connection.prepareStatement(selectStatement);
            ResultSet rs = statement.executeQuery(selectStatement);
            if (!rs.next() ) {
                return false;
            }
        }
        catch (SQLException e) {
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
        return true;   
    }
    public void createForum() throws Exception {
        PreparedStatement statement = null;
        try {
            String insertStatement = "Insert into forum(latitude,longitude,radius,title,authorId,description,categoryId,creationDate,isActive) "
                    + "values (?,?,?,?,?,?,?,now(),?)";
            Connection connection = Connection_class.createConnection();
            statement = connection.prepareStatement(insertStatement);
            statement.setDouble(1, latitude);
            statement.setDouble(2, longitude);
            statement.setInt(3, radius);
            statement.setString(4, title);
            statement.setInt(5, creatorId);
            statement.setString(6, description);
            statement.setInt(7, categoryId);
            statement.setBoolean(9, isActive);
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

    public void deleteForum(String forumName) throws Exception {
        PreparedStatement statement = null;
      if(checkIfForumExists(forumName)) {
          try {
              String deleteStatement = "Delete from forum where title = " + forumName;
              Connection connection = Connection_class.createConnection();
              statement = connection.prepareStatement(deleteStatement);
              statement.executeUpdate();
              connection.commit();
          }
          catch (SQLException e) {
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
    }

    void createForumSubscription(int accessLevel) {
        // SNS Implementation
    }

    void deleteForumSubscription() {
        // SNS Implementation
    }

}
