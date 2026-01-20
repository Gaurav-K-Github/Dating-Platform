<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Messages - Dating Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .conversation-item {
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .conversation-item:hover {
            background-color: #f8f9fa;
        }
        .user-photo {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: #6c757d;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 18px;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-4">
        <h2>Messages</h2>
        
        <c:if test="${unreadCount > 0}">
            <div class="alert alert-info">
                You have ${unreadCount} unread message(s)
            </div>
        </c:if>
        
        <c:choose>
            <c:when test="${not empty mutualMatches}">
                <div class="list-group">
                    <c:forEach items="${mutualMatches}" var="match">
                        <a href="conversation?userId=${match.userId}" class="list-group-item list-group-item-action conversation-item">
                            <div class="d-flex align-items-center">
                                <div class="user-photo me-3">
                                    ${match.firstName.substring(0,1)}${match.lastName.substring(0,1)}
                                </div>
                                <div class="flex-grow-1">
                                    <h5 class="mb-0">${match.firstName} ${match.lastName}</h5>
                                    <c:if test="${not empty lastMessages[match.userId]}">
                                        <p class="text-muted mb-0 small">
                                            <c:choose>
                                                <c:when test="${lastMessages[match.userId].senderId == userId}">
                                                    You: ${lastMessages[match.userId].messageText.length() > 50 ? 
                                                            lastMessages[match.userId].messageText.substring(0, 50).concat("...") : 
                                                            lastMessages[match.userId].messageText}
                                                </c:when>
                                                <c:otherwise>
                                                    ${lastMessages[match.userId].messageText.length() > 50 ? 
                                                      lastMessages[match.userId].messageText.substring(0, 50).concat("...") : 
                                                      lastMessages[match.userId].messageText}
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </c:if>
                                </div>
                                <div>
                                    <span class="badge bg-primary">Chat</span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <h4>No conversations yet</h4>
                    <p>Start matching with people to begin messaging!</p>
                    <a href="matches" class="btn btn-primary">Find Matches</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
