package com.dating.dao;

import com.dating.config.DatabaseConfig;
import com.dating.model.Preferences;

import java.sql.*;

/**
 * DAO class for Preferences operations
 */
public class PreferencesDAO {

    private static final String INSERT_PREFERENCES = 
        "INSERT INTO preferences (user_id, preferred_gender, min_age, max_age, max_distance) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_PREFERENCES_BY_USER_ID = "SELECT * FROM preferences WHERE user_id = ?";
    private static final String UPDATE_PREFERENCES = 
        "UPDATE preferences SET preferred_gender = ?, min_age = ?, max_age = ?, max_distance = ? WHERE user_id = ?";
    private static final String DELETE_PREFERENCES = "DELETE FROM preferences WHERE user_id = ?";
    private static final String UPSERT_PREFERENCES = 
        "INSERT INTO preferences (user_id, preferred_gender, min_age, max_age, max_distance) " +
        "VALUES (?, ?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE " +
        "preferred_gender = VALUES(preferred_gender), min_age = VALUES(min_age), " +
        "max_age = VALUES(max_age), max_distance = VALUES(max_distance)";

    /**
     * Save or update preferences (upsert)
     */
    public boolean savePreferences(Preferences preferences) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPSERT_PREFERENCES)) {
            
            preparedStatement.setInt(1, preferences.getUserId());
            preparedStatement.setString(2, preferences.getPreferredGender());
            preparedStatement.setInt(3, preferences.getMinAge());
            preparedStatement.setInt(4, preferences.getMaxAge());
            preparedStatement.setInt(5, preferences.getMaxDistance());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get preferences for a user
     */
    public Preferences getPreferencesByUserId(int userId) {
        Preferences preferences = null;
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PREFERENCES_BY_USER_ID)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                preferences = new Preferences();
                preferences.setUserId(rs.getInt("user_id"));
                preferences.setPreferredGender(rs.getString("preferred_gender"));
                preferences.setMinAge(rs.getInt("min_age"));
                preferences.setMaxAge(rs.getInt("max_age"));
                preferences.setMaxDistance(rs.getInt("max_distance"));
                preferences.setCreatedAt(rs.getTimestamp("created_at"));
                preferences.setUpdatedAt(rs.getTimestamp("updated_at"));
            } else {
                // Return default preferences if none exist
                preferences = new Preferences();
                preferences.setUserId(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Return default preferences on error
            preferences = new Preferences();
            preferences.setUserId(userId);
        }
        return preferences;
    }

    /**
     * Delete preferences for a user
     */
    public boolean deletePreferences(int userId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PREFERENCES)) {
            
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
