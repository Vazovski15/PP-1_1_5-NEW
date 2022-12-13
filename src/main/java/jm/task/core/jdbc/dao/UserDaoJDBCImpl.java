package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    Connection connection = Util.getConnection();



    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                    " name VARCHAR(45)," + " lastName VARCHAR(45), age TINYINT(3))");
            connection.commit();
        } catch (SQLException e) {

        }
    }

    private void rollbackQuietly(Connection c) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            connection.commit();
        } catch (SQLException e) {

        }


    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users (name, lastName, age) values (?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            connection.commit();


        } catch (SQLException e) {
            rollbackQuietly(connection);
        }

    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id= ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            rollbackQuietly(connection);
        }


    }

    public List<User> getAllUsers() {
        ArrayList<User> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name, lastName, age FROM users ")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                result.add(user);

                connection.commit();

            }

        } catch (SQLException e) {
            rollbackQuietly(connection);
            result = new ArrayList<>();
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");

            connection.commit();
        } catch (SQLException e) {
            rollbackQuietly(connection);

        }


    }
}
