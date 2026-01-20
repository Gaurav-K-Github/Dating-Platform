<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conversation with ${otherUser.firstName} - Dating Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .chat-container {
            height: 500px;
            overflow-y: auto;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            background: #f8f9fa;
        }
        .message {
            margin-bottom: 15px;
            display: flex;
        }
        .message.sent {
            justify-content: flex-end;
        }
        .message.received {
            justify-content: flex-start;
        }
        .message-bubble {
            max-width: 60%;
            padding: 10px 15px;
            border-radius: 15px;
            word-wrap: break-word;
        }
        .message.sent .message-bubble {
            background: #007bff;
            color: white;
        }
        .message.received .message-bubble {
            background: white;
            border: 1px solid #ddd;
        }
        .message-time {
            font-size: 0.75rem;
            color: #6c757d;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex align-items-center mb-3">
            <a href="messages" class="btn btn-outline-secondary me-3">← Back</a>
            <h2 class="mb-0">Conversation with ${otherUser.firstName} ${otherUser.lastName}</h2>
        </div>
        
        <div class="chat-container mb-3" id="chatContainer">
            <c:choose>
                <c:when test="${not empty conversation}">
                    <c:forEach items="${conversation}" var="msg">
                        <div class="message ${msg.senderId == currentUserId ? 'sent' : 'received'}">
                            <div>
                                <div class="message-bubble">
                                    ${msg.messageText}
                                </div>
                                <div class="message-time ${msg.senderId == currentUserId ? 'text-end' : ''}">
                                    <fmt:formatDate value="${msg.sentAt}" pattern="MMM dd, HH:mm"/>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-center text-muted">No messages yet. Start the conversation!</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <form method="post" action="send-message">
            <input type="hidden" name="receiverId" value="${otherUser.userId}">
            <div class="input-group">
                <input type="text" class="form-control" name="messageText" 
                       placeholder="Type your message..." required maxlength="1000">
                <button type="submit" class="btn btn-primary">Send</button>
            </div>
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Auto-scroll to bottom of chat
        window.onload = function() {
            var chatContainer = document.getElementById('chatContainer');
            chatContainer.scrollTop = chatContainer.scrollHeight;
        };
        
        // Auto-refresh every 10 seconds for new messages
        setTimeout(function() {
            location.reload();
        }, 10000);
    </script>
</body>
</html>
