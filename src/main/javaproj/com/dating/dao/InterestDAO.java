package com.dating.dao;

import com.dating.config.DatabaseConfig;
import com.dating.model.Interest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Interest operations
 */
public class InterestDAO {

    private static final String SELECT_ALL_INTERESTS = "SELECT * FROM interests ORDER BY interest_name";
    private static final String SELECT_INTEREST_BY_ID = "SELECT * FROM interests WHERE interest_id = ?";
    private static final String SELECT_USER_INTERESTS = 
        "SELECT i.* FROM interests i " +
        "INNER JOIN user_interests ui ON i.interest_id = ui.interest_id " +
        "WHERE ui.user_id = ? ORDER BY i.interest_name";

    /**
     * Get all available interests
     */
    public List<Interest> getAllInterests() {
        List<Interest> interests = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INTERESTS);
             ResultSet rs = preparedStatement.executeQuery()) {
            
            while (rs.next()) {
                Interest interest = new Interest();
                interest.setInterestId(rs.getInt("interest_id"));
                interest.setInterestName(rs.getString("interest_name"));
                interests.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }

    /**
     * Get interest by ID
     */
    public Interest getInterestById(int interestId) {
        Interest interest = null;
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INTEREST_BY_ID)) {
            
            preparedStatement.setInt(1, interestId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                interest = new Interest();
                interest.setInterestId(rs.getInt("interest_id"));
                interest.setInterestName(rs.getString("interest_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interest;
    }

    /**
     * Get all interests for a specific user
     */
    public List<Interest> getUserInterests(int userId) {
        List<Interest> interests = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_INTERESTS)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                Interest interest = new Interest();
                interest.setInterestId(rs.getInt("interest_id"));
                interest.setInterestName(rs.getString("interest_name"));
                interests.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }
}
