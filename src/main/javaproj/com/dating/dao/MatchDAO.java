package com.dating.dao;

import com.dating.config.DatabaseConfig;
import com.dating.model.Gender;
import com.dating.model.Match;
import com.dating.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Match operations
 */
public class MatchDAO {

    private static final String INSERT_MATCH = 
        "INSERT INTO matches (user_id, target_user_id, match_status) VALUES (?, ?, ?)";
    private static final String GET_MATCH = 
        "SELECT * FROM matches WHERE user_id = ? AND target_user_id = ?";
    private static final String UPDATE_MATCH_STATUS = 
        "UPDATE matches SET match_status = ? WHERE match_id = ?";
    private static final String GET_USER_MATCHES = 
        "SELECT * FROM matches WHERE user_id = ? AND match_status = 'MATCHED' ORDER BY created_at DESC";
    private static final String GET_MUTUAL_MATCHES_WITH_USERS = 
        "SELECT DISTINCT u.* FROM users u " +
        "INNER JOIN matches m1 ON u.user_id = m1.target_user_id " +
        "INNER JOIN matches m2 ON u.user_id = m2.user_id " +
        "WHERE m1.user_id = ? AND m1.match_status = 'MATCHED' " +
        "AND m2.target_user_id = ? AND m2.match_status = 'MATCHED' " +
        "ORDER BY u.user_id DESC";
    private static final String GET_LIKED_USER_IDS = 
        "SELECT target_user_id FROM matches WHERE user_id = ? AND match_status IN ('LIKED', 'MATCHED')";
    private static final String GET_PASSED_USER_IDS = 
        "SELECT target_user_id FROM matches WHERE user_id = ? AND match_status = 'PASSED'";
    private static final String CHECK_ALREADY_INTERACTED = 
        "SELECT COUNT(*) FROM matches WHERE user_id = ? AND target_user_id = ?";

    /**
     * Create a new match (like or pass)
     */
    public boolean createMatch(Match match) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MATCH, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, match.getUserId());
            preparedStatement.setInt(2, match.getTargetUserId());
            preparedStatement.setString(3, match.getMatchStatus());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    match.setMatchId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get a specific match between two users
     */
    public Match getMatch(int userId, int targetUserId) {
        Match match = null;
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MATCH)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, targetUserId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                match = new Match();
                match.setMatchId(rs.getInt("match_id"));
                match.setUserId(rs.getInt("user_id"));
                match.setTargetUserId(rs.getInt("target_user_id"));
                match.setMatchStatus(rs.getString("match_status"));
                match.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }

    /**
     * Update match status
     */
    public boolean updateMatchStatus(int matchId, String status) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MATCH_STATUS)) {
            
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, matchId);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if two users have mutually matched
     */
    public boolean checkMutualMatch(int user1Id, int user2Id) {
        Match user1Like = getMatch(user1Id, user2Id);
        Match user2Like = getMatch(user2Id, user1Id);
        
        return user1Like != null && user2Like != null && 
               user1Like.getMatchStatus().equals("LIKED") && 
               user2Like.getMatchStatus().equals("LIKED");
    }

    /**
     * Get all mutual matches for a user
     */
    public List<User> getMutualMatches(int userId) {
        List<User> matches = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MUTUAL_MATCHES_WITH_USERS)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                
                // Convert java.sql.Date to LocalDate
                java.sql.Date sqlDate = rs.getDate("date_of_birth");
                if (sqlDate != null) {
                    user.setDateOfBirth(sqlDate.toLocalDate());
                }
                
                // Convert String to Gender enum
                String genderStr = rs.getString("gender");
                if ("MALE".equals(genderStr)) {
                    user.setGender(Gender.MALE);
                } else if ("FEMALE".equals(genderStr)) {
                    user.setGender(Gender.FEMALE);
                } else {
                    user.setGender(Gender.OTHER);
                }
                
                matches.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    /**
     * Get IDs of users the current user has already liked or passed
     */
    public List<Integer> getInteractedUserIds(int userId) {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT target_user_id FROM matches WHERE user_id = ?";
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                userIds.add(rs.getInt("target_user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    /**
     * Check if user has already interacted with target user
     */
    public boolean hasAlreadyInteracted(int userId, int targetUserId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ALREADY_INTERACTED)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, targetUserId);
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
     * Get matched users with their details for dashboard display
     */
    public List<User> getMatchedUsers(int userId, int limit) {
        List<User> matchedUsers = new ArrayList<>();
        String sql = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.date_of_birth, u.gender, m.created_at " +
                     "FROM matches m " +
                     "JOIN users u ON m.target_user_id = u.user_id " +
                     "WHERE m.user_id = ? AND m.match_status = 'MATCHED' " +
                     "ORDER BY m.created_at DESC " +
                     "LIMIT ?";
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, limit);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                
                // Convert java.sql.Date to LocalDate
                java.sql.Date sqlDate = rs.getDate("date_of_birth");
                if (sqlDate != null) {
                    user.setDateOfBirth(sqlDate.toLocalDate());
                }
                
                // Convert String to Gender enum
                String genderStr = rs.getString("gender");
                if ("MALE".equals(genderStr)) {
                    user.setGender(Gender.MALE);
                } else if ("FEMALE".equals(genderStr)) {
                    user.setGender(Gender.FEMALE);
                } else {
                    user.setGender(Gender.OTHER);
                }
                
                matchedUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchedUsers;
    }
}
