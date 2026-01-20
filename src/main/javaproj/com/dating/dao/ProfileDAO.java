package com.dating.dao;

import com.dating.config.DatabaseConfig;
import com.dating.model.Profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Profile operations
 */
public class ProfileDAO {

    // SQL Queries
    private static final String INSERT_PROFILE = "INSERT INTO profiles (user_id, bio, location, profile_photo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_PROFILE_BY_USER_ID = "SELECT * FROM profiles WHERE user_id = ?";
    private static final String UPDATE_PROFILE = "UPDATE profiles SET bio = ?, location = ?, profile_photo = ? WHERE user_id = ?";
    private static final String DELETE_PROFILE = "DELETE FROM profiles WHERE user_id = ?";
    private static final String PROFILE_EXISTS = "SELECT COUNT(*) FROM profiles WHERE user_id = ?";

    /**
     * Create a new profile for a user
     */
    public boolean createProfile(Profile profile) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROFILE)) {
            
            preparedStatement.setInt(1, profile.getUserId());
            preparedStatement.setString(2, profile.getBio());
            preparedStatement.setString(3, profile.getLocation());
            preparedStatement.setString(4, profile.getProfilePhoto());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get profile by user ID
     */
    public Profile getProfileByUserId(int userId) {
        Profile profile = null;
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROFILE_BY_USER_ID)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                profile = new Profile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setBio(rs.getString("bio"));
                profile.setLocation(rs.getString("location"));
                profile.setProfilePhoto(rs.getString("profile_photo"));
                profile.setCreatedAt(rs.getTimestamp("created_at"));
                profile.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    /**
     * Update an existing profile
     */
    public boolean updateProfile(Profile profile) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROFILE)) {
            
            preparedStatement.setString(1, profile.getBio());
            preparedStatement.setString(2, profile.getLocation());
            preparedStatement.setString(3, profile.getProfilePhoto());
            preparedStatement.setInt(4, profile.getUserId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if profile exists for user
     */
    public boolean profileExists(int userId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(PROFILE_EXISTS)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a profile
     */
    public boolean deleteProfile(int userId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROFILE)) {
            
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add an interest for a user
     */
    public boolean addUserInterest(int userId, int interestId) {
        String sql = "INSERT INTO user_interests (user_id, interest_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE user_id=user_id";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, interestId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove an interest from a user
     */
    public boolean removeUserInterest(int userId, int interestId) {
        String sql = "DELETE FROM user_interests WHERE user_id = ? AND interest_id = ?";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, interestId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clear all interests for a user
     */
    public boolean clearUserInterests(int userId) {
        String sql = "DELETE FROM user_interests WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all interest IDs for a user
     */
    public List<Integer> getUserInterestIds(int userId) {
        List<Integer> interestIds = new ArrayList<>();
        String sql = "SELECT interest_id FROM user_interests WHERE user_id = ?";
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                interestIds.add(rs.getInt("interest_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interestIds;
    }
}
