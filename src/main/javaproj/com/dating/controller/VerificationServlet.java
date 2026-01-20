package com.dating.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.dating.dao.UserDAO;
import com.dating.dao.VerificationTokenDAO;
import com.dating.model.VerificationToken;

/**
 * Servlet for handling email verification
 */
@WebServlet("/verify")
public class VerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private VerificationTokenDAO tokenDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        tokenDAO = new VerificationTokenDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("verificationError", "Invalid verification link");
            request.getRequestDispatcher("verification-result.jsp").forward(request, response);
            return;
        }

        VerificationToken verificationToken = tokenDAO.getTokenByValue(token);
        
        if (verificationToken == null) {
            request.setAttribute("verificationError", "Invalid or expired verification token");
            request.getRequestDispatcher("verification-result.jsp").forward(request, response);
            return;
        }

        if (verificationToken.isUsed()) {
            request.setAttribute("verificationError", "This verification link has already been used");
            request.getRequestDispatcher("verification-result.jsp").forward(request, response);
            return;
        }

        if (verificationToken.isExpired()) {
            request.setAttribute("verificationError", "This verification link has expired. Please request a new one.");
            request.getRequestDispatcher("verification-result.jsp").forward(request, response);
            return;
        }

        // Mark email as verified
        boolean updated = userDAO.markEmailAsVerified(verificationToken.getUserId());
        
        if (updated) {
            // Mark token as used
            tokenDAO.markAsUsed(verificationToken.getTokenId());
            
            request.setAttribute("verificationSuccess", true);
            request.setAttribute("message", "Your email has been successfully verified! You can now log in.");
        } else {
            request.setAttribute("verificationError", "An error occurred during verification. Please try again.");
        }
        
        request.getRequestDispatcher("verification-result.jsp").forward(request, response);
    }
}
