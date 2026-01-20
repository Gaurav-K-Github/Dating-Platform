<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.time.LocalDate,java.time.Period" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Matches - Dating Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .match-card {
            cursor: pointer;
            transition: transform 0.2s;
        }
        .match-card:hover {
            transform: scale(1.02);
        }
        .match-photo {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: #6c757d;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 24px;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-4">
        <h2>My Matches</h2>
        <p class="text-muted">People you've mutually matched with</p>
        
        <c:choose>
            <c:when test="${not empty mutualMatches}">
                <div class="row">
                    <c:forEach items="${mutualMatches}" var="match">
                        <%@ page import="java.time.LocalDate, java.time.Period" %>
                        <%
                            com.dating.model.User match = (com.dating.model.User) pageContext.getAttribute("match");
                            LocalDate birthDate = match.getDateOfBirth();
                            int age = Period.between(birthDate, LocalDate.now()).getYears();
                            pageContext.setAttribute("age", age);
                        %>
                        <div class="col-md-6 col-lg-4 mb-3">
                            <div class="card match-card">
                                <div class="card-body">
                                    <div class="d-flex align-items-center">
                                        <div class="match-photo me-3">
                                            ${match.firstName.substring(0,1)}${match.lastName.substring(0,1)}
                                        </div>
                                        <div>
                                            <h5 class="card-title mb-0">${match.firstName} ${match.lastName}</h5>
                                            <p class="text-muted mb-0">${age} years old</p>
                                        </div>
                                    </div>
                                    <div class="mt-3">
                                        <a href="conversation?userId=${match.userId}" class="btn btn-primary btn-sm w-100">
                                            Send Message
                                        </a>
                                        <a href="profile?action=view&userId=${match.userId}" class="btn btn-outline-secondary btn-sm w-100 mt-2">
                                            View Profile
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <h4>No matches yet</h4>
                    <p>Start discovering potential matches and when you both like each other, you'll see them here!</p>
                    <a href="matches" class="btn btn-primary">Start Discovering</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
