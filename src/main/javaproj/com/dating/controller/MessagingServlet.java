package com.dating.controller;

import com.dating.dao.*;
import com.dating.model.*;
import com.dating.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * MessagingServlet handles messaging between matched users
 */
@WebServlet(urlPatterns = {"/messages", "/conversation", "/send-message"})
public class MessagingServlet extends HttpServlet {

    private MessageDAO messageDAO;
    private UserDAO userDAO;
    private MatchDAO matchDAO;

    @Override
    public void init() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();
        matchDAO = new MatchDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String path = request.getServletPath();
        
        if ("/conversation".equals(path)) {
            String otherUserIdParam = request.getParameter("userId");
            if (otherUserIdParam != null) {
                try {
                    int otherUserId = Integer.parseInt(otherUserIdParam);
                    showConversation(request, response, userId, otherUserId);
                } catch (NumberFormatException e) {
                    response.sendRedirect("messages");
                }
            } else {
                response.sendRedirect("messages");
            }
        } else {
            // Show messages inbox
            showInbox(request, response, userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String receiverIdParam = request.getParameter("receiverId");
        String messageText = request.getParameter("messageText");
        
        if (receiverIdParam == null || messageText == null || messageText.trim().isEmpty()) {
            response.sendRedirect("messages");
            return;
        }
        
        try {
            int receiverId = Integer.parseInt(receiverIdParam);
            
            // Verify users are matched before allowing messaging
            if (!areUsersMatched(userId, receiverId)) {
                request.setAttribute("error", "You can only message users you've matched with");
                response.sendRedirect("messages");
                return;
            }
            
            // Sanitize message
            messageText = ValidationUtil.sanitizeInput(messageText);
            
            // Send message
            Message message = new Message(userId, receiverId, messageText);
            boolean sent = messageDAO.sendMessage(message);
            
            if (sent) {
                response.sendRedirect("conversation?userId=" + receiverId);
            } else {
                request.setAttribute("error", "Failed to send message");
                response.sendRedirect("conversation?userId=" + receiverId);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("messages");
        }
    }

    private void showInbox(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Get all conversation partners (users with mutual matches)
        List<User> mutualMatches = matchDAO.getMutualMatches(userId);
        
        // Get unread message count
        int unreadCount = messageDAO.getUnreadCount(userId);
        
        // For each mutual match, get the last message
        Map<Integer, Message> lastMessages = new HashMap<>();
        for (User match : mutualMatches) {
            List<Message> conversation = messageDAO.getConversation(userId, match.getUserId());
            if (!conversation.isEmpty()) {
                lastMessages.put(match.getUserId(), conversation.get(conversation.size() - 1));
            }
        }
        
        request.setAttribute("mutualMatches", mutualMatches);
        request.setAttribute("lastMessages", lastMessages);
        request.setAttribute("unreadCount", unreadCount);
        
        request.getRequestDispatcher("messages.jsp").forward(request, response);
    }

    private void showConversation(HttpServletRequest request, HttpServletResponse response, 
                                  int userId, int otherUserId)
            throws ServletException, IOException {
        
        // Verify users are matched
        if (!areUsersMatched(userId, otherUserId)) {
            response.sendRedirect("messages");
            return;
        }
        
        // Get conversation
        List<Message> conversation = messageDAO.getConversation(userId, otherUserId);
        
        // Mark messages as read
        messageDAO.markAsRead(userId, otherUserId);
        
        // Get other user info
        User otherUser = userDAO.selectUser(otherUserId);
        
        request.setAttribute("conversation", conversation);
        request.setAttribute("otherUser", otherUser);
        request.setAttribute("currentUserId", userId);
        
        request.getRequestDispatcher("conversation.jsp").forward(request, response);
    }

    private boolean areUsersMatched(int user1Id, int user2Id) {
        Match match1 = matchDAO.getMatch(user1Id, user2Id);
        Match match2 = matchDAO.getMatch(user2Id, user1Id);
        
        return match1 != null && match2 != null && 
               "MATCHED".equals(match1.getMatchStatus()) && 
               "MATCHED".equals(match2.getMatchStatus());
    }
}
