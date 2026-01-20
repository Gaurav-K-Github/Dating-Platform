package com.dating.controller;

import com.dating.dao.*;
import com.dating.model.*;
import com.dating.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ProfileServlet handles user profile operations
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private ProfileDAO profileDAO;
    private InterestDAO interestDAO;
    private PreferencesDAO preferencesDAO;

    @Override
    public void init() {
        profileDAO = new ProfileDAO();
        interestDAO = new InterestDAO();
        preferencesDAO = new PreferencesDAO();
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
        String action = request.getParameter("action");
        
        if ("view".equals(action)) {
            // View another user's profile
            String targetUserIdParam = request.getParameter("userId");
            if (targetUserIdParam != null) {
                try {
                    int targetUserId = Integer.parseInt(targetUserIdParam);
                    showUserProfile(request, response, targetUserId);
                } catch (NumberFormatException e) {
                    response.sendRedirect("dashboard");
                }
            } else {
                response.sendRedirect("dashboard");
            }
        } else {
            // View/Edit own profile
            showProfileForm(request, response, userId);
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
        
        // Get form data
        String bio = request.getParameter("bio");
        String location = request.getParameter("location");
        String[] selectedInterests = request.getParameterValues("interests");
        
        // Preferences
        String preferredGender = request.getParameter("preferredGender");
        String minAgeStr = request.getParameter("minAge");
        String maxAgeStr = request.getParameter("maxAge");
        
        // Validate and sanitize
        bio = ValidationUtil.sanitizeInput(bio);
        location = ValidationUtil.sanitizeInput(location);
        
        // Create or update profile
        Profile existingProfile = profileDAO.getProfileByUserId(userId);
        
        if (existingProfile == null) {
            // Create new profile
            Profile newProfile = new Profile(userId, bio, location, "default-avatar.jpg");
            profileDAO.createProfile(newProfile);
        } else {
            // Update existing profile
            existingProfile.setBio(bio);
            existingProfile.setLocation(location);
            profileDAO.updateProfile(existingProfile);
        }
        
        // Update interests
        profileDAO.clearUserInterests(userId);
        if (selectedInterests != null) {
            for (String interestId : selectedInterests) {
                try {
                    int id = Integer.parseInt(interestId);
                    profileDAO.addUserInterest(userId, id);
                } catch (NumberFormatException e) {
                    // Skip invalid interest IDs
                }
            }
        }
        
        // Update preferences
        Preferences preferences = new Preferences();
        preferences.setUserId(userId);
        preferences.setPreferredGender(preferredGender != null ? preferredGender : "Any");
        
        try {
            preferences.setMinAge(minAgeStr != null ? Integer.parseInt(minAgeStr) : 18);
            preferences.setMaxAge(maxAgeStr != null ? Integer.parseInt(maxAgeStr) : 100);
        } catch (NumberFormatException e) {
            preferences.setMinAge(18);
            preferences.setMaxAge(100);
        }
        
        preferencesDAO.savePreferences(preferences);
        
        request.setAttribute("success", "Profile updated successfully!");
        showProfileForm(request, response, userId);
    }

    private void showProfileForm(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Get profile
        Profile profile = profileDAO.getProfileByUserId(userId);
        if (profile == null) {
            profile = new Profile();
            profile.setUserId(userId);
        }
        
        // Get all interests
        List<Interest> allInterests = interestDAO.getAllInterests();
        
        // Get user's selected interests
        List<Integer> userInterestIds = profileDAO.getUserInterestIds(userId);
        
        // Get preferences
        Preferences preferences = preferencesDAO.getPreferencesByUserId(userId);
        
        request.setAttribute("profile", profile);
        request.setAttribute("allInterests", allInterests);
        request.setAttribute("userInterestIds", userInterestIds);
        request.setAttribute("preferences", preferences);
        
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    private void showUserProfile(HttpServletRequest request, HttpServletResponse response, int targetUserId)
            throws ServletException, IOException {
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.selectUser(targetUserId);
        Profile profile = profileDAO.getProfileByUserId(targetUserId);
        List<Interest> interests = interestDAO.getUserInterests(targetUserId);
        
        request.setAttribute("viewUser", user);
        request.setAttribute("viewProfile", profile);
        request.setAttribute("viewInterests", interests);
        
        request.getRequestDispatcher("view-profile.jsp").forward(request, response);
    }
}
