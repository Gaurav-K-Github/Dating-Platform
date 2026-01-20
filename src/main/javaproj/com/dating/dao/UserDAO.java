package com.dating.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dating.config.DatabaseConfig;
import com.dating.model.Gender;
import com.dating.model.User;

/**
 * Data Access Object for User entity
 * Handles all database operations related to users
 */
public class UserDAO {

    private String jdbcURL = DatabaseConfig.JDBC_URL;
    private String jdbcUsername = DatabaseConfig.JDBC_USERNAME;
    private String jdbcPassword = DatabaseConfig.JDBC_PASSWORD;

    private static final String INSERT_USER_SQL = "INSERT INTO users (first_name, last_name, email, password, date_of_birth, gender) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE user_id = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, date_of_birth = ?, gender = ? WHERE user_id = ?;";
    private static final String UPDATE_EMAIL_VERIFIED = "UPDATE users SET email_verified = TRUE WHERE user_id = ?;";

    public UserDAO() {
        super();
    }

    /**
     * Establishes and returns a database connection
     * @return Connection object
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Inserts a new user into the database
     * @param user User object to insert
     */
    public void insertUser(User user) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, Date.valueOf(user.getDateOfBirth()));
            preparedStatement.setString(6, user.getGender().toString());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user by their ID
     * @param id User ID
     * @return User object or null if not found
     */
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                Gender gender = Gender.fromString(resultSet.getString("gender"));
                LocalDate registrationDate = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                boolean emailVerified = resultSet.getBoolean("email_verified");

                user = new User(id, firstName, lastName, email, password, dateOfBirth, gender, registrationDate, emailVerified);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves a user by their email
     * @param email User email
     * @return User object or null if not found
     */
    public User selectUserByEmail(String email) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                Gender gender = Gender.fromString(resultSet.getString("gender"));
                LocalDate registrationDate = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                boolean emailVerified = resultSet.getBoolean("email_verified");

                user = new User(id, firstName, lastName, email, password, dateOfBirth, gender, registrationDate, emailVerified);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves all users from the database
     * @return List of all users
     */
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                Gender gender = Gender.fromString(resultSet.getString("gender"));
                LocalDate registrationDate = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                boolean emailVerified = resultSet.getBoolean("email_verified");

                users.add(new User(id, firstName, lastName, email, password, dateOfBirth, gender, registrationDate, emailVerified));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Deletes a user from the database
     * @param id User ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(int id) {
        boolean status = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            status = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Updates an existing user in the database
     * @param user User object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        boolean status = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, Date.valueOf(user.getDateOfBirth()));
            preparedStatement.setString(6, user.getGender().toString());
            preparedStatement.setInt(7, user.getUserId());

            int rowsAffected = preparedStatement.executeUpdate();
            status = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Marks a user's email as verified
     * @param userId User ID
     * @return true if update was successful, false otherwise
     */
    public boolean markEmailAsVerified(int userId) {
        boolean status = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMAIL_VERIFIED)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            status = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
