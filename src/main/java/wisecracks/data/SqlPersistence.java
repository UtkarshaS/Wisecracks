package main.java.wisecracks.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlPersistence {
    public Connection connection;

    public SqlPersistence() {
        this.connection = createConnection();
    }

//    public static void main(String... rags) {
//        //Post post = new Post(1,2,"shweta2311","Hello everyone",new Date(0));
//        SqlPersistence sql = new SqlPersistence();
//        //sql.insertPost(post);
//        String s = new String("shweta23");
//        ArrayList<Forum> al = sql.getAllForums(-1.265872350,2.578358770,"116282651442588691605");
//        for(Forum f: al){
//            for(Post p: f.relatedPosts) {
//                System.out.print(p.forumId);
//                System.out.println(p.postId);
//            }
//        }
//        //sql.deleteForum(1);
//    }

    // region User related methods

    public boolean checkUserExists(int userId) {
        if (getUser(userId) != null) {
            return true;
        }
        return false;

    }

    public void insertUser(User user) {
        PreparedStatement statement = null;
        try {
            String insertStatement = "Insert into User(userId,firstName,lastName,addressLine1,addressLine2,city,country, profile,registerationEndPoint) "
                    + "values (?,?,?,?,?,?,?,?,now(),?)";
            statement = connection.prepareStatement(insertStatement);
            statement.setString(1, user.userId);
            statement.setString(2, user.firstName);
            statement.setString(3, user.lastName);
            statement.setString(4, user.addressLine1);
            statement.setString(5, user.addressLine2);
            statement.setString(6, user.city);
            statement.setString(7, user.country);
            statement.setString(8, user.profile);
            statement.setString(9, user.registerationEndPoint);
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

    public User getUser(int userId) {
        PreparedStatement statement = null;
        User newUser = null;
        ResultSet rs = null;
        try {
           // System.out.println("hi");
            String selectStatement = "Select firstName,lastName,addressLine1,addressLine2,city,country, profile, recentvisitedTime from User "
                    + " where userId = " + userId;
            statement = connection.prepareStatement(selectStatement);
           // System.out.println(selectStatement);
            rs = statement.executeQuery(selectStatement);

            while (rs.next()) {
                newUser = new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("addressLine1"),
                        rs.getString("addressLine2"), rs.getString("city"), rs.getString("country"),
                        rs.getString("profile"), rs.getString("recentvisitedTime"),rs.getString("registerationEndPoint"));
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

    // endregion

    // region Forum related methods

    public void insertForum(Forum forum) {

        PreparedStatement statement = null;

        try {
            String insertProcedure = "{call insert_forum_and_subscriber(?,?,?,?,?,?,?,?,?)}";
            connection.prepareCall(insertProcedure);
            statement = connection.prepareStatement(insertProcedure);
            statement.setDouble(1, forum.latitude);
            statement.setDouble(2, forum.longitude);
            statement.setDouble(3, forum.radius);
            statement.setString(4, forum.title);
            statement.setString(5, forum.userId);
            statement.setString(6, forum.topicARN);
            statement.setString(7, forum.description);
            statement.setInt(8, forum.categoryId);
            statement.setBoolean(9, forum.isActive);
            statement.executeUpdate();
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
    public ArrayList<Forum> getAllForums(double latitude, double longitude,String userId) {
        ArrayList<Forum> forumResult = new ArrayList<Forum>();
        forumResult.addAll(getForumsInArea(latitude,longitude));
        forumResult.addAll(getForums(userId));
        return forumResult;
    }
    
    // related to location
    public ArrayList<Forum> getForumsInArea(double latitude, double longitude) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Statement stmt = null;
        ResultSet postResultSet = null;
        ArrayList<Forum> forumResult = new ArrayList<Forum>();
        
        try {
            String selectForumProcedure = "{call get_forums_in_area(?,?)}";
            statement = connection.prepareCall(selectForumProcedure);
            statement.setDouble(1,latitude);
            statement.setDouble(2,longitude);
            rs = statement.executeQuery();
            System.out.println("I am here");
            while (rs.next()) {
                Forum t = new Forum(rs.getInt("forumId"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                        rs.getDouble("radius"), rs.getString("title"), rs.getString("userId"),
                        rs.getString("topicARN"), rs.getString("description"), rs.getInt("categoryId"),
                        rs.getDate("creationDate"));
                forumResult.add(t);
            }
            System.out.println("I am here now");
            stmt = connection.createStatement();
            for(Forum existing : forumResult) {
               postResultSet = stmt.executeQuery("select postId,forumId,userId,textBody,creationDate from Post where forumId = " + existing.forumId);
               List<Post> postList = new ArrayList<Post>();
               while(postResultSet.next()) {
                   Post p= new Post(postResultSet.getInt("postId"), postResultSet.getInt("forumId"),
                           postResultSet.getString("userId"), postResultSet.getString("textBody"), postResultSet.getDate("creationDate"));
                   postList.add(p);
               }
               existing.relatedPosts = postList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    postResultSet.close();  
                    rs.close();
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return forumResult;
    }

    // related to userId
    public ArrayList<Forum> getForums(String userId) {
        PreparedStatement statement = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet postResultSet = null;
        ArrayList<Forum> forumResult = new ArrayList<Forum>();
        try {
            String selectStatement = "select forumId, latitude, longitude,radius, title, userId ,topicARN, description,categoryId, creationDate from Forum where userId = ?";
            statement = connection.prepareStatement(selectStatement);
            statement.setString(1,userId);
            rs = statement.executeQuery();
            while (rs.next()) {
                Forum t = new Forum(rs.getInt("forumId"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                        rs.getDouble("radius"), rs.getString("title"), rs.getString("userId"),
                        rs.getString("topicARN"), rs.getString("description"), rs.getInt("categoryId"),
                        rs.getDate("creationDate"));
               forumResult.add(t);
            }
            stmt = connection.createStatement();
            for(Forum existing : forumResult) {
               postResultSet = stmt.executeQuery("select postId,forumId,userId,textBody,creationDate from Post where forumId = " + existing.forumId);
               List<Post> postList = new ArrayList<Post>();
               while(postResultSet.next()) {
                   Post p= new Post(postResultSet.getInt("postId"), postResultSet.getInt("forumId"),
                           postResultSet.getString("userId"), postResultSet.getString("textBody"), postResultSet.getDate("creationDate"));
                   postList.add(p);
               }
               existing.relatedPosts = postList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    postResultSet.close();  
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    stmt.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return forumResult;
    }

    // related to category and location

    public void deleteForum(int forumId) {
        PreparedStatement statement = null;
        try {
            String deleteStatement = "delete from Forum where forumId = " + forumId;
            statement = connection.prepareStatement(deleteStatement);
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

    public void createForumSubscription(int accessLevel) {
        // SNS Implementation
    }

    public void deleteForumSubscription() {
        // SNS Implementation
    }

    // endregion

    // region Post related methods

    public ArrayList<Post> getPosts(int forumId) {
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<Post> postResult = new ArrayList<Post>();
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("select postId, forumId, userId , textBody, creationDate from Post where forumId  = "
                            + forumId);
            while (rs.next()) {
                Post t = new Post(rs.getInt("postId"), rs.getInt("forumId"),
                        rs.getString("userId"), rs.getString("textBody"), rs.getDate("creationDate"));
                postResult.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return postResult;
    }

    public void insertPost(Post post) {

        CallableStatement statement = null;

        try {
            String insertProcedure = "{call insert_post_and_subscriber(?,?,?)}";
            statement = connection.prepareCall(insertProcedure);
            statement.setInt(1, post.forumId);
            statement.setString(2, post.userId);
            statement.setString(3, post.textBody);
            ResultSet rd = statement.executeQuery();
            System.out.println(rd);
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

    public void deletePost(int postId) {

    }

    // endregion

    private Connection createConnection() {
        Connection connection = null;
        String userName = "cloud_root";
        String password = "cloud_root";
        String hostname = "cloudproject.cwopwu80kdhw.us-east-1.rds.amazonaws.com";
        String port = "3306";
        String dbName = "Wisecracks";
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password="
                + password;
        try {
            //DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            System.out.println("Sql connection borked in SQLException SqlPersistence.java");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Sql connection borked in ClassNotFoundException SqlPersistence.java");
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
  
}
