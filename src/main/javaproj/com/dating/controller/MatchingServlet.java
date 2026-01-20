package com.dating.controller;

import com.dating.dao.*;
import com.dating.model.*;
import com.dating.service.MatchingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * MatchingServlet handles finding and managing matches
 */
@WebServlet(urlPatterns = {"/matches", "/my-matches", "/like", "/pass"})
public class MatchingServlet extends HttpServlet {

    private MatchingService matchingService;
    private UserDAO userDAO;
    private ProfileDAO profileDAO;
    private InterestDAO interestDAO;

    @Override
    public void init() {
        matchingService = new MatchingService();
        userDAO = new UserDAO();
        profileDAO = new ProfileDAO();
        interestDAO = new InterestDAO();
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
        
        if ("/my-matches".equals(path)) {
            showMutualMatches(request, response, userId);
        } else {
            showPotentialMatches(request, response, userId);
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
        String path = request.getServletPath();
        String targetUserIdParam = request.getParameter("targetUserId");
        
        if (targetUserIdParam == null) {
            response.sendRedirect("matches");
            return;
        }
        
        try {
            int targetUserId = Integer.parseInt(targetUserIdParam);
            
            if ("/like".equals(path)) {
                // Process like
                boolean isMutualMatch = matchingService.processLike(userId, targetUserId);
                
                if (isMutualMatch) {
                    // It's a match!
                    User matchedUser = userDAO.selectUser(targetUserId);
                    request.setAttribute("matchedUser", matchedUser);
                    request.setAttribute("isNewMatch", true);
                }
                
                response.sendRedirect("matches");
                
            } else if ("/pass".equals(path)) {
                // Process pass
                matchingService.processPass(userId, targetUserId);
                response.sendRedirect("matches");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("matches");
        }
    }

    private void showPotentialMatches(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Get potential matches (10 at a time)
        List<User> potentialMatches = matchingService.findPotentialMatches(userId, 10);
        
        // For each match, get their profile and interests
        for (User match : potentialMatches) {
            Profile profile = profileDAO.getProfileByUserId(match.getUserId());
            List<Interest> interests = interestDAO.getUserInterests(match.getUserId());
            match.setPassword(null); // Clear password for security
        }
        
        request.setAttribute("potentialMatches", potentialMatches);
        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }

    private void showMutualMatches(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Get mutual matches
        List<User> mutualMatches = matchingService.getMutualMatches(userId);
        
        // For each match, get their profile
        for (User match : mutualMatches) {
            Profile profile = profileDAO.getProfileByUserId(match.getUserId());
            List<Interest> interests = interestDAO.getUserInterests(match.getUserId());
            match.setPassword(null); // Clear password for security
        }
        
        request.setAttribute("mutualMatches", mutualMatches);
        request.getRequestDispatcher("my-matches.jsp").forward(request, response);
    }
}
