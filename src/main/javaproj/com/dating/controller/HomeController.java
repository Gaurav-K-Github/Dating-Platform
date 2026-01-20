package com.dating.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.dating.dao.UserDAO;
import com.dating.model.Gender;
import com.dating.model.User;

/**
 * Main servlet controller for handling all application routes
 */
@WebServlet(urlPatterns = {"/", "/login", "/loginprocess", "/logout", "/dashboard", "/list", "/new", "/insert", "/delete", "/edit", "/update"})
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDao;

    @Override
    public void init() {
        userDao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/register":
                    showRegistrationForm(request, response);
                    break;
                case "/login":
                    showLoginForm(request, response);
                    break;
                case "/loginprocess":
                    loginProcess(request, response);
                    break;
                case "/logout":
                    logout(request, response);
                    break;
                case "/dashboard":
                    showDashboard(request, response);
                    break;
                case "/list":
                    listUsers(request, response);
                    break;
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    editUser(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    showLoginForm(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/loginprocess".equals(action)) {
            loginProcess(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }

    private void loginProcess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        StringBuilder debugInfo = new StringBuilder();

        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("loginError", "⚠️ Email and password are required");
            debugInfo.append("Email empty: ").append(email == null || email.trim().isEmpty())
                     .append(" | Password empty: ").append(password == null || password.isEmpty());
            request.setAttribute("debugInfo", debugInfo.toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String originalEmail = email;
        email = email.trim().toLowerCase();
        
        debugInfo.append("Original email: '").append(originalEmail).append("'<br>")
                 .append("Normalized email: '").append(email).append("'<br>")
                 .append("Input password length: ").append(password.length()).append("<br>");
        
        // Get user by email
        User user = userDao.selectUserByEmail(email);
        
        // Check if user exists
        if (user == null) {
            request.setAttribute("loginError", "❌ No account found with email: " + email);
            request.setAttribute("loginErrorDetail", "Please check your email or register a new account");
            debugInfo.append("User lookup: NULL (no user found in database)");
            request.setAttribute("debugInfo", debugInfo.toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        debugInfo.append("User found: ID=").append(user.getUserId())
                 .append(" | Name=").append(user.getFirstName()).append(" ").append(user.getLastName())
                 .append("<br>Email in DB: '").append(user.getEmail()).append("'<br>")
                 .append("Email verified: ").append(user.isEmailVerified()).append("<br>")
                 .append("DB Password: '").append(user.getPassword()).append("'<br>")
                 .append("Input Password: '").append(password).append("'<br>");
        
        // Verify password
        if (!password.equals(user.getPassword())) {
            debugInfo.append("Password verification result: FAILED (passwords do not match)").append("<br>");
            request.setAttribute("loginError", "❌ Incorrect password");
            request.setAttribute("loginErrorDetail", "Please try again or reset your password");
            request.setAttribute("debugInfo", debugInfo.toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        debugInfo.append("Password verification result: SUCCESS").append("<br>");
        
        // Successful login
        HttpSession session = request.getSession();
        session.setAttribute("status", "active");
        session.setAttribute("email", user.getEmail());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());
        session.setAttribute("emailVerified", user.isEmailVerified());
        session.setAttribute("user", user);
        
        response.sendRedirect("dashboard");
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login");
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isLoggedIn(request)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("Dashboard.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isLoggedIn(request)) {
            List<User> users = userDao.selectAllUsers();
            request.setAttribute("listUsers", users);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String genderStr = request.getParameter("gender");

        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
        Gender gender = Gender.fromString(genderStr);

        User newUser = new User(0, firstName, lastName, email, password, dateOfBirth, gender, LocalDate.now(), false);
        userDao.insertUser(newUser);
        response.sendRedirect("list");
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.selectUser(id);
        request.setAttribute("user", existingUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("edit.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String genderStr = request.getParameter("gender");

        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
        Gender gender = Gender.fromString(genderStr);

        User updatedUser = new User(id, firstName, lastName, email, password, dateOfBirth, gender, null, false);
        userDao.updateUser(updatedUser);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(id);
        response.sendRedirect("list");
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "active".equals(session.getAttribute("status"));
    }

    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
        dispatcher.forward(request, response);
    }
}
