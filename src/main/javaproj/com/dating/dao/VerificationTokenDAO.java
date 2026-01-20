package com.dating.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.dating.config.DatabaseConfig;
import com.dating.model.VerificationToken;

/**
 * Data Access Object for VerificationToken
 */
public class VerificationTokenDAO {

    private static final String INSERT_TOKEN = "INSERT INTO verification_tokens (user_id, token, expires_at) VALUES (?, ?, ?)";
    private static final String SELECT_BY_TOKEN = "SELECT * FROM verification_tokens WHERE token = ? AND used = FALSE";
    private static final String MARK_AS_USED = "UPDATE verification_tokens SET used = TRUE WHERE token_id = ?";
    private static final String DELETE_EXPIRED = "DELETE FROM verification_tokens WHERE expires_at < NOW() OR used = TRUE";

    private UserDAO userDAO;

    public VerificationTokenDAO() {
        this.userDAO = new UserDAO();
    }

    /**
     * Creates a new verification token
     */
    public void createToken(int userId, String token, LocalDateTime expiresAt) {
        try (Connection conn = userDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TOKEN)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, token);
            stmt.setTimestamp(3, Timestamp.valueOf(expiresAt));
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a token by its string value
     */
    public VerificationToken getTokenByValue(String tokenValue) {
        VerificationToken token = null;
        
        try (Connection conn = userDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_TOKEN)) {
            
            stmt.setString(1, tokenValue);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                token = new VerificationToken(
                    rs.getInt("token_id"),
                    rs.getInt("user_id"),
                    rs.getString("token"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("expires_at").toLocalDateTime(),
                    rs.getBoolean("used")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return token;
    }

    /**
     * Marks a token as used
     */
    public void markAsUsed(int tokenId) {
        try (Connection conn = userDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(MARK_AS_USED)) {
            
            stmt.setInt(1, tokenId);
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes expired and used tokens
     */
    public void cleanupTokens() {
        try (Connection conn = userDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_EXPIRED)) {
            
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
