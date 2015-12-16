package main.java.wisecracks.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlPersistence {
    public Connection connection;

    public SqlPersistence() {
	this.connection = createConnection();
    }

    public static void main(String... rags) {
	SqlPersistence sql = new SqlPersistence();
	System.out.println(sql.checkUserExists(1));
    }

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
	    String insertStatement = "Insert into User(userId,firstName,lastName,addressLine1,addressLine2,city,country, profile) "
		    + "values (?,?,?,?,?,?,?,?,now())";
	    statement = connection.prepareStatement(insertStatement);
	    statement.setString(1, user.userId);
	    statement.setString(2, user.firstName);
	    statement.setString(3, user.lastName);
	    statement.setString(4, user.addressLine1);
	    statement.setString(5, user.addressLine2);
	    statement.setString(6, user.city);
	    statement.setString(7, user.country);
	    statement.setString(8, user.profile);
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
	    System.out.println("hi");
	    String selectStatement = "Select firstName,lastName,addressLine1,addressLine2,city,country, profile, recentvisitedTime from User "
		    + " where userId = " + userId;
	    statement = connection.prepareStatement(selectStatement);
	    System.out.println(selectStatement);
	    rs = statement.executeQuery(selectStatement);

	    while (rs.next()) {
		newUser = new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("addressLine1"),
		        rs.getString("addressLine2"), rs.getString("city"), rs.getString("country"),
		        rs.getString("profile"), rs.getString("recentvisitedTime"));
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
	    String insertStatement = "Insert into forum(latitude,longitude,title,authorId,description,categoryId,creationDate,isActive) "
		    + "values (?,?,?,?,?,?,?,now(),?)";
	    statement = connection.prepareStatement(insertStatement);
	    statement.setDouble(1, forum.latitude);
	    statement.setDouble(2, forum.longitude);
	    statement.setString(3, forum.title);
	    statement.setInt(4, forum.userId);
	    statement.setString(5, forum.description);
	    statement.setInt(6, forum.categoryId);
	    statement.setBoolean(7, forum.isActive);
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

    public ArrayList<Forum> getForumsInArea(double latitude, double longitude) {
	return null;
    }

    public ArrayList<Forum> getForums(int userId) {
	Statement statement = null;
	ResultSet rs = null;
	ArrayList<Forum> forumResult = new ArrayList<Forum>();
	try {
	    statement = connection.createStatement();
	    rs = statement
		    .executeQuery("select forumId, latitude, longitude, title, authorId as userId ,description,categoryId, creationDate from Forum where authorId  = "
		            + userId);
	    while (rs.next()) {
		Forum t = new Forum(rs.getInt("forumId"), rs.getDouble("latitude"), rs.getDouble("longitude"),
		        rs.getString("title"), rs.getInt(userId), rs.getString("description"), rs.getInt("categoryId"),
		        rs.getDate("creationDate"));
		forumResult.add(t);
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
	return forumResult;
    }

    public void deleteForum(int forumId) {
	PreparedStatement statement = null;
	try {
	    String deleteStatement = "delete from forum where forumId = " + forumId;
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

	    rs = statement
		    .executeQuery("select postId, forumId, topic, creatorId as userId , textBody, creationDate from Post where forumId  = "
		            + forumId);
	    while (rs.next()) {
		Post t = new Post(rs.getInt("postId"), rs.getInt("forumId"), rs.getString("topic"),
		        rs.getInt("userId"), rs.getString("textBody"), rs.getDate("creationDate"));
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
	    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

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
