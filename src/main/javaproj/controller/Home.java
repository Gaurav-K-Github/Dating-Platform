package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/*"})
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Home() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        String action = request.getServletPath();

        switch (action) {
            case "/register":
                dispatcher = request.getRequestDispatcher("/registration.jsp");
                dispatcher.forward(request, response);
                break;
                
            case "/login":
                dispatcher = request.getRequestDispatcher("/login.jsp");
                dispatcher.forward(request, response);
                break;
                
            case "/dashboard":
                dispatcher = request.getRequestDispatcher("/Dashboard.jsp");
                dispatcher.forward(request, response);
                break;    

            default:
                dispatcher = request.getRequestDispatcher("/login.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submissions if necessary
    }
}
