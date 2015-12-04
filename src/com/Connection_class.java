package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection_class {
    public static Connection createConnection() throws Exception {
        Connection connection = null;
        String userName = "cloud_root";
        String password = "cloud_root";
        String hostname = "cloudproject.cwopwu80kdhw.us-east-1.rds.amazonaws.com";
        String port = "3306";
        String dbName = "CloudProject";
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password="
                + password;
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcUrl);
        return connection;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}