package com.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.user.model.User;

public class UserDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/dating_platform";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Admin@123";

    private static final String INSERT_USER_SQL = "INSERT INTO users (first_name, last_name, email, password, date_of_birth, gender) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE user_id = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, date_of_birth = ?, gender = ? WHERE user_id = ?;";

    public UserDAO() {
        super();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

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
                Gender gender = Gender.valueOf(resultSet.getString("gender"));
                LocalDate registrationDate = resultSet.getTimestamp("registration_date").toLocalDateTime().toLocalDate();

                user = new User(id, firstName, lastName, email, password, dateOfBirth, gender, registrationDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

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
                Gender gender = Gender.valueOf(resultSet.getString("gender"));
                LocalDate registrationDate = resultSet.getTimestamp("registration_date").toLocalDateTime().toLocalDate();

                users.add(new User(id, firstName, lastName, email, password, dateOfBirth, gender, registrationDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

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
}
