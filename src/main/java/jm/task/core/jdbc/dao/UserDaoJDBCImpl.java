package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    {
        try {
            connection = Util.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS USERS (
                 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                 name VARCHAR(255),
                 lastName VARCHAR(255),
                 age TINYINT
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS USERS";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = """
                       INSERT INTO USERS (name, lastName, age)
                       VALUES (?, ?, ?)
                       """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        System.out.printf("User с именем – %s добавлен в базу данных%n", name);
    }

    public void removeUserById(long id) {
        String deleteQuery = "DELETE FROM USERS WHERE id = ?";
        String updateQuery = "UPDATE USERS SET id = id - 1 WHERE id > ?";

        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteQuery);
             PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery)) {
            deletePreparedStatement.setLong(1, id);
            deletePreparedStatement.executeUpdate();
            updatePreparedStatement.setLong(1, id);
            updatePreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM USERS";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                userList.add(new User() {
                    {
                        setId(resultSet.getLong("id"));
                        setName(resultSet.getString("name"));
                        setLastName(resultSet.getString("lastName"));
                        setAge(resultSet.getByte("age"));
                    }
                });
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return userList;
    }

    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE USERS";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
