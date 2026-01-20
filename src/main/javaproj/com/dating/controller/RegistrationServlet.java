package com.dating.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dating.dao.UserDAO;
import com.dating.model.Gender;
import com.dating.model.User;
import com.dating.util.ValidationUtil;

/**
 * Servlet for handling user registration
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDao;

    @Override
    public void init() {
        userDao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show registration form
        RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> errors = new ArrayList<>();
        
        // Get form parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String genderStr = request.getParameter("gender");

        // Validate inputs
        if (firstName == null || !ValidationUtil.isValidName(firstName)) {
            errors.add("First name must be 2-50 characters and contain only letters");
        }
        
        if (lastName == null || !ValidationUtil.isValidName(lastName)) {
            errors.add("Last name must be 2-50 characters and contain only letters");
        }
        
        if (email == null || !ValidationUtil.isValidEmail(email)) {
            errors.add("Please enter a valid email address");
        } else {
            // Check if email already exists
            User existingUser = userDao.selectUserByEmail(email);
            if (existingUser != null) {
                errors.add("Email address is already registered");
            }
        }
        
        if (password == null || !ValidationUtil.isValidPassword(password)) {
            errors.add(ValidationUtil.getPasswordRequirements());
        }
        
        if (!ValidationUtil.passwordsMatch(password, confirmPassword)) {
            errors.add("Passwords do not match");
        }
        
        LocalDate dateOfBirth = null;
        try {
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                dateOfBirth = LocalDate.parse(dateOfBirthStr);
                if (!ValidationUtil.isValidAge(dateOfBirth)) {
                    errors.add("You must be at least 18 years old to register");
                }
            } else {
                errors.add("Date of birth is required");
            }
        } catch (Exception e) {
            errors.add("Invalid date of birth format");
        }
        
        Gender gender = null;
        try {
            if (genderStr != null && !genderStr.isEmpty()) {
                gender = Gender.fromString(genderStr);
            } else {
                errors.add("Gender is required");
            }
        } catch (IllegalArgumentException e) {
            errors.add("Invalid gender selection");
        }

        // If there are validation errors, return to registration form
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("dateOfBirth", dateOfBirthStr);
            request.setAttribute("gender", genderStr);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Sanitize inputs
        firstName = ValidationUtil.sanitizeInput(firstName);
        lastName = ValidationUtil.sanitizeInput(lastName);
        email = email.trim().toLowerCase();

        // Create new user
        User newUser = new User(0, firstName, lastName, email, password, 
                               dateOfBirth, gender, LocalDate.now(), false);

        try {
            userDao.insertUser(newUser);
            
            // Set success message
            request.getSession().setAttribute("successMessage", 
                "Registration successful! Please log in.");
            
            response.sendRedirect("login");
        } catch (Exception e) {
            errors.add("An error occurred during registration. Please try again.");
            request.setAttribute("errors", errors);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        }
    }
}
