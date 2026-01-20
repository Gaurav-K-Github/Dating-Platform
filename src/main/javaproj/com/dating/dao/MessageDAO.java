package com.dating.dao;

import com.dating.config.DatabaseConfig;
import com.dating.model.Message;
import com.dating.model.User;
import com.dating.model.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Message operations
 */
public class MessageDAO {

    private static final String INSERT_MESSAGE = 
        "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
    private static final String GET_CONVERSATION = 
        "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
        "ORDER BY sent_at ASC";
    private static final String MARK_AS_READ = 
        "UPDATE messages SET is_read = TRUE WHERE receiver_id = ? AND sender_id = ? AND is_read = FALSE";
    private static final String GET_UNREAD_COUNT = 
        "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = FALSE";
    private static final String GET_CONVERSATIONS = 
        "SELECT DISTINCT " +
        "CASE WHEN m.sender_id = ? THEN m.receiver_id ELSE m.sender_id END AS other_user_id, " +
        "MAX(m.sent_at) AS last_message_time " +
        "FROM messages m " +
        "WHERE m.sender_id = ? OR m.receiver_id = ? " +
        "GROUP BY other_user_id " +
        "ORDER BY last_message_time DESC";

    /**
     * Send a message
     */
    public boolean sendMessage(Message message) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, message.getSenderId());
            preparedStatement.setInt(2, message.getReceiverId());
            preparedStatement.setString(3, message.getMessageText());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessageId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get conversation between two users
     */
    public List<Message> getConversation(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CONVERSATION)) {
            
            preparedStatement.setInt(1, user1Id);
            preparedStatement.setInt(2, user2Id);
            preparedStatement.setInt(3, user2Id);
            preparedStatement.setInt(4, user1Id);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setMessageText(rs.getString("message_text"));
                message.setSentAt(rs.getTimestamp("sent_at"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Mark all messages from a specific sender as read
     */
    public boolean markAsRead(int receiverId, int senderId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(MARK_AS_READ)) {
            
            preparedStatement.setInt(1, receiverId);
            preparedStatement.setInt(2, senderId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get count of unread messages for a user
     */
    public int getUnreadCount(int userId) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_UNREAD_COUNT)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get list of user IDs that current user has conversations with
     */
    public List<Integer> getConversationPartners(int userId) {
        List<Integer> partnerIds = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CONVERSATIONS)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, userId);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                partnerIds.add(rs.getInt("other_user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partnerIds;
    }

    /**
     * Get recent conversation partners with user details for dashboard display
     */
    public List<User> getRecentConversationUsers(int userId, int limit) {
        List<User> conversationUsers = new ArrayList<>();
        String sql = "SELECT DISTINCT u.user_id, u.first_name, u.last_name, u.email, u.date_of_birth, u.gender, " +
                     "MAX(m.created_at) AS last_message_time " +
                     "FROM users u " +
                     "JOIN messages m ON (u.user_id = m.sender_id OR u.user_id = m.receiver_id) " +
                     "WHERE (m.sender_id = ? OR m.receiver_id = ?) AND u.user_id != ? " +
                     "GROUP BY u.user_id, u.first_name, u.last_name, u.email, u.date_of_birth, u.gender " +
                     "ORDER BY last_message_time DESC " +
                     "LIMIT ?";
        
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.JDBC_URL, DatabaseConfig.JDBC_USERNAME, DatabaseConfig.JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, userId);
            preparedStatement.setInt(4, limit);
            
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
                
                conversationUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversationUsers;
    }
}
