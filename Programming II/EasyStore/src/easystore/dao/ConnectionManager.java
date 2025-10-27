package easystore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL = "jdbc:sqlite:easystore.db";
    private static Connection instance;

    private ConnectionManager() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static synchronized void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                instance = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
