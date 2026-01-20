package com.dating.service;

import com.dating.dao.*;
import com.dating.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Matching Service - Contains the matching algorithm
 * This is the core business logic for finding compatible users
 */
public class MatchingService {

    private UserDAO userDAO;
    private PreferencesDAO preferencesDAO;
    private MatchDAO matchDAO;
    private ProfileDAO profileDAO;

    public MatchingService() {
        this.userDAO = new UserDAO();
        this.preferencesDAO = new PreferencesDAO();
        this.matchDAO = new MatchDAO();
        this.profileDAO = new ProfileDAO();
    }

    /**
     * Find potential matches for a user based on their preferences
     * Algorithm considers:
     * 1. Gender preference
     * 2. Age range preference
     * 3. Excludes users already liked/passed
     * 4. Excludes the current user
     * 5. Only shows users with profiles
     * 
     * @param userId - The user looking for matches
     * @param limit - Maximum number of matches to return
     * @return List of potential match users
     */
    public List<User> findPotentialMatches(int userId, int limit) {
        List<User> potentialMatches = new ArrayList<>();
        
        // Get user's preferences
        Preferences prefs = preferencesDAO.getPreferencesByUserId(userId);
        User currentUser = userDAO.selectUser(userId);
        
        if (currentUser == null) {
            return potentialMatches;
        }
        
        // Get users already interacted with
        List<Integer> interactedUserIds = matchDAO.getInteractedUserIds(userId);
        
        // Build SQL query based on preferences
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT u.* FROM users u ");
        sql.append("INNER JOIN profiles p ON u.user_id = p.user_id ");
        sql.append("WHERE u.user_id != ? ");
        
        // Add already interacted users to exclusion
        if (!interactedUserIds.isEmpty()) {
            sql.append("AND u.user_id NOT IN (");
            for (int i = 0; i < interactedUserIds.size(); i++) {
                sql.append("?");
                if (i < interactedUserIds.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") ");
        }
        
        // Filter by gender preference
        if (!"ANY".equals(prefs.getPreferredGender())) {
            sql.append("AND u.gender = ? ");
        }
        
        sql.append("ORDER BY u.user_id DESC LIMIT ?");
        
        try (Connection connection = DriverManager.getConnection(
                com.dating.config.DatabaseConfig.JDBC_URL,
                com.dating.config.DatabaseConfig.JDBC_USERNAME,
                com.dating.config.DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            preparedStatement.setInt(paramIndex++, userId);
            
            // Set excluded user IDs
            for (Integer excludedId : interactedUserIds) {
                preparedStatement.setInt(paramIndex++, excludedId);
            }
            
            // Set gender preference
            if (!"ANY".equals(prefs.getPreferredGender())) {
                // Convert preference to match database ENUM format
                String genderFilter = convertGenderPreference(prefs.getPreferredGender());
                preparedStatement.setString(paramIndex++, genderFilter);
            }
            
            preparedStatement.setInt(paramIndex++, limit);
            
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
                
                // Filter by age preference
                int age = calculateAge(sqlDate);
                if (age >= prefs.getMinAge() && age <= prefs.getMaxAge()) {
                    potentialMatches.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return potentialMatches;
    }

    /**
     * Process a like action and check for mutual match
     * 
     * @param userId - User who is liking
     * @param targetUserId - User being liked
     * @return true if it's a mutual match, false otherwise
     */
    public boolean processLike(int userId, int targetUserId) {
        // Create the like match
        Match match = new Match(userId, targetUserId, "LIKED");
        boolean created = matchDAO.createMatch(match);
        
        if (!created) {
            return false;
        }
        
        // Check if it's a mutual match
        if (matchDAO.checkMutualMatch(userId, targetUserId)) {
            // Update both matches to MATCHED status
            Match userMatch = matchDAO.getMatch(userId, targetUserId);
            Match targetMatch = matchDAO.getMatch(targetUserId, userId);
            
            if (userMatch != null && targetMatch != null) {
                matchDAO.updateMatchStatus(userMatch.getMatchId(), "MATCHED");
                matchDAO.updateMatchStatus(targetMatch.getMatchId(), "MATCHED");
                return true; // It's a match!
            }
        }
        
        return false; // Like recorded but not a mutual match
    }

    /**
     * Process a pass action
     */
    public boolean processPass(int userId, int targetUserId) {
        Match match = new Match(userId, targetUserId, "PASSED");
        return matchDAO.createMatch(match);
    }

    /**
     * Get all mutual matches for a user
     */
    public List<User> getMutualMatches(int userId) {
        return matchDAO.getMutualMatches(userId);
    }

    /**
     * Calculate age from date of birth
     */
    private int calculateAge(java.sql.Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        LocalDate birthDate = dateOfBirth.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    /**
     * Convert preference gender to database format
     */
    private String convertGenderPreference(String preference) {
        // Database uses uppercase ENUM values
        return preference; // Return as-is (MALE, FEMALE, OTHER, ANY)
    }

    /**
     * Get count of available potential matches
     */
    public int getPotentialMatchCount(int userId) {
        List<User> matches = findPotentialMatches(userId, 1000);
        return matches.size();
    }
}
