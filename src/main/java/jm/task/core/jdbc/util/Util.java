package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/user",
                "root",
                "root");
        connection.setAutoCommit(false);

        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
