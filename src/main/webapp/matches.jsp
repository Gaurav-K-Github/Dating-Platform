<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.time.LocalDate,java.time.Period" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Discover Matches - Soulmate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modern-style.css">
    <style>
        body {
            background: var(--neutral-bg);
        }
        
        .matches-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 2rem;
        }
        
        .match-card {
            background: white;
            border-radius: var(--border-radius-lg);
            padding: 2rem;
            box-shadow: var(--soft-shadow);
            text-align: center;
        }
        
        .profile-photo {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background: var(--primary-gradient);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 48px;
            margin: 0 auto 1.5rem;
            font-weight: 600;
        }
        
        .match-name {
            font-size: 2rem;
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: var(--text-primary);
        }
        
        .match-gender {
            font-size: 1.1rem;
            color: var(--text-muted);
            margin-bottom: 2rem;
        }
        
        .action-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }
        
        .btn-pass {
            background: rgba(239, 68, 68, 0.1);
            color: #ef4444;
            border: 2px solid #ef4444;
        }
        
        .btn-pass:hover {
            background: #ef4444;
            color: white;
        }
        
        .btn-view {
            background: rgba(59, 130, 246, 0.1);
            color: #3b82f6;
            border: 2px solid #3b82f6;
        }
        
        .btn-view:hover {
            background: #3b82f6;
            color: white;
        }
        
        .no-matches {
            text-align: center;
            padding: 3rem 2rem;
            background: white;
            border-radius: var(--border-radius-lg);
            box-shadow: var(--soft-shadow);
        }
        
        .no-matches h4 {
            color: var(--text-primary);
            margin-bottom: 1rem;
        }
        
        .no-matches p {
            color: var(--text-muted);
            margin-bottom: 2rem;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div style="max-width: 1200px; margin: 0 auto; width: 100%; display: flex; justify-content: space-between; align-items: center;">
            <div class="navbar-brand">💘 Soulmate</div>
            <div style="display: flex; gap: var(--space-3);">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">Dashboard</a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link">Profile</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link" style="color: var(--error-color);">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="matches-container">
        <h2 style="margin-bottom: 1.5rem;">Discover Matches</h2>
        <p style="color: var(--text-muted); margin-bottom: 2rem;">Like or pass on potential matches based on your preferences</p>
        
        <c:choose>
            <c:when test="${not empty potentialMatches}">
                <c:forEach items="${potentialMatches}" var="match" varStatus="status">
                    <c:if test="${status.index == 0}">
                        <%
                            com.dating.model.User match = (com.dating.model.User) pageContext.getAttribute("match");
                            LocalDate birthDate = match.getDateOfBirth();
                            int age = Period.between(birthDate, LocalDate.now()).getYears();
                            pageContext.setAttribute("age", age);
                        %>
                        <div class="match-card">
                            <div class="profile-photo">
                                ${match.firstName.substring(0,1)}${match.lastName.substring(0,1)}
                            </div>
                            
                            <h3 class="match-name">${match.firstName} ${match.lastName}, ${age}</h3>
                            <p class="match-gender">
                                <c:if test="${match.gender.name() == 'MALE'}">♂</c:if>
                                <c:if test="${match.gender.name() == 'FEMALE'}">♀</c:if>
                                ${match.gender}
                            </p>
                            
                            <div class="action-buttons">
                                <form method="post" action="pass" style="display: inline;">
                                    <input type="hidden" name="targetUserId" value="${match.userId}">
                                    <button type="submit" class="btn btn-pass">
                                        ✕ Pass
                                    </button>
                                </form>
                                
                                <a href="profile?action=view&userId=${match.userId}" class="btn btn-view">
                                    ℹ View Profile
                                </a>
                                
                                <form method="post" action="like" style="display: inline;">
                                    <input type="hidden" name="targetUserId" value="${match.userId}">
                                    <button type="submit" class="btn btn-primary">
                                        ♥ Like
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-matches">
                    <h4>No more matches available</h4>
                    <p>You've seen all available users based on your preferences. Check back later or adjust your preferences in your profile.</p>
                    <a href="profile" class="btn btn-primary">Update Preferences</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
