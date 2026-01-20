<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="com.dating.dao.MatchDAO" %>
<%@ page import="com.dating.dao.MessageDAO" %>
<%@ page import="com.dating.model.User" %>
<%@ page import="java.util.List" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Integer userId = (Integer) session.getAttribute("userId");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    String email = (String) session.getAttribute("email");
    Boolean emailVerified = (Boolean) session.getAttribute("emailVerified");
    if (firstName == null) firstName = "User";
    if (lastName == null) lastName = "";
    if (emailVerified == null) emailVerified = false;
    
    // Generate username from email (part before @)
    String username = email != null ? "@" + email.split("@")[0] : "@user";
    
    // Get matched users from database
    MatchDAO matchDAO = new MatchDAO();
    List<User> matchedUsers = matchDAO.getMatchedUsers(userId, 6);
    request.setAttribute("matchedUsers", matchedUsers);
    
    // Get message data from database
    MessageDAO messageDAO = new MessageDAO();
    int unreadMessageCount = messageDAO.getUnreadCount(userId);
    List<User> recentConversations = messageDAO.getRecentConversationUsers(userId, 3);
    int totalConversationCount = messageDAO.getConversationPartners(userId).size();
    request.setAttribute("unreadMessageCount", unreadMessageCount);
    request.setAttribute("recentConversations", recentConversations);
    request.setAttribute("totalConversationCount", totalConversationCount);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Discover - Soulmate</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: #0f0f12;
            color: white;
            overflow: hidden;
            height: 100vh;
            position: relative;
        }

        /* Background Gradient Animation */
        .bg-image {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(135deg, #1a1a2e 0%, #4a2c5e 25%, #2d1b3d 50%, #2a3f5f 75%, #1a1f2e 100%);
            background-size: 400% 400%;
            animation: gradientShift 25s ease-in-out infinite;
            filter: blur(100px);
            opacity: 0.5;
            z-index: 0;
        }

        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }

        /* Ambient Glowing Blobs */
        .blob {
            position: fixed;
            border-radius: 50%;
            filter: blur(100px);
            opacity: 0.25;
            z-index: 0;
            animation: float 30s ease-in-out infinite;
        }

        .blob-1 {
            width: 500px;
            height: 500px;
            background: radial-gradient(circle, rgba(186, 85, 211, 0.4), transparent);
            top: -10%;
            left: -10%;
        }

        .blob-2 {
            width: 400px;
            height: 400px;
            background: radial-gradient(circle, rgba(138, 43, 226, 0.4), transparent);
            bottom: -10%;
            right: -10%;
            animation-delay: -10s;
        }

        .blob-3 {
            width: 350px;
            height: 350px;
            background: radial-gradient(circle, rgba(147, 112, 219, 0.4), transparent);
            top: 50%;
            left: 50%;
            animation-delay: -20s;
        }

        @keyframes float {
            0%, 100% {
                transform: translate(0, 0) scale(1);
            }
            33% {
                transform: translate(50px, -50px) scale(1.1);
            }
            66% {
                transform: translate(-30px, 30px) scale(0.9);
            }
        }

        /* Main Container */
        .container {
            display: flex;
            height: 100vh;
            position: relative;
            z-index: 1;
        }

        /* Glass Effect Utility */
        .glass {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 24px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        }

        /* LEFT PANEL - Discovery Feed */
        .left-panel {
            flex: 0 0 75%;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem;
        }

        /* Logo */
        .logo {
            position: absolute;
            top: 1.5rem;
            left: 1.5rem;
            padding: 0.75rem 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-weight: 600;
            font-size: 1.1rem;
            z-index: 10;
        }

        .logo-icon {
            font-size: 1.5rem;
        }

        /* Card Stack */
        .card-stack {
            position: relative;
            width: 420px;
            height: 580px;
        }

        .swipe-card {
            position: absolute;
            width: 100%;
            height: 100%;
            border-radius: 32px;
            overflow: hidden;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .swipe-card.back-1 {
            transform: translateY(16px) scale(0.95);
            filter: brightness(0.7);
            z-index: 1;
        }

        .swipe-card.back-2 {
            transform: translateY(32px) scale(0.9);
            filter: brightness(0.5);
            z-index: 0;
        }

        .swipe-card.main {
            z-index: 2;
            cursor: grab;
        }

        .swipe-card.main:active {
            cursor: grabbing;
        }

        .swipe-card img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .card-overlay {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            padding: 2rem;
            background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
        }

        .card-name {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .card-bio {
            font-size: 0.95rem;
            color: rgba(255, 255, 255, 0.9);
            margin-bottom: 0.25rem;
        }

        .card-status {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.85rem;
            color: #4ade80;
        }

        .status-dot {
            width: 8px;
            height: 8px;
            background: #4ade80;
            border-radius: 50%;
            animation: pulse 2s ease-in-out infinite;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.5; }
        }

        /* Action Buttons */
        .actions {
            position: absolute;
            bottom: -80px;
            left: 50%;
            transform: translateX(-50%);
            display: flex;
            gap: 1.5rem;
            z-index: 3;
        }

        .action-btn {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        }

        .action-btn:hover {
            transform: scale(1.1);
        }

        .action-btn.pass {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            color: #ef4444;
        }

        .action-btn.star {
            background: rgba(59, 130, 246, 0.15);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(59, 130, 246, 0.3);
            color: #3b82f6;
            width: 56px;
            height: 56px;
        }

        .action-btn.like {
            background: linear-gradient(135deg, #ff6b9d, #ff8fab);
            color: white;
            box-shadow: 0 8px 32px rgba(255, 107, 157, 0.4);
        }

        /* RIGHT PANEL - Sidebar */
        .right-panel {
            flex: 0 0 25%;
            padding: 1.5rem;
            display: flex;
            flex-direction: column;
            gap: 1rem;
            overflow-y: auto;
            scrollbar-width: none;
            -ms-overflow-style: none;
        }

        .right-panel::-webkit-scrollbar {
            display: none;
        }

        /* Profile Card */
        .profile-card {
            padding: 1.5rem;
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .profile-avatar {
            position: relative;
            width: 64px;
            height: 64px;
            cursor: pointer;
            transition: transform 0.2s ease;
        }

        .profile-avatar:hover {
            transform: scale(1.05);
        }

        .avatar-img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background: linear-gradient(135deg, #ff6b9d, #a855f7);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            color: white;
            border: 2px solid rgba(255, 107, 157, 0.5);
        }

        .profile-info {
            flex: 1;
        }

        .profile-name {
            font-weight: 600;
            font-size: 1.1rem;
            margin-bottom: 0.25rem;
        }

        .profile-subtitle {
            font-size: 0.85rem;
            color: #9ca3af;
        }

        .verification-badge {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.75rem;
            font-weight: bold;
        }

        .verified {
            background: rgba(74, 222, 128, 0.2);
            color: #4ade80;
            border: 2px solid #4ade80;
        }

        .not-verified {
            background: rgba(239, 68, 68, 0.2);
            color: #ef4444;
            border: 2px solid #ef4444;
        }

        /* Messages Pill Header */
        .messages-pill {
            background: rgba(255, 255, 255, 0.08);
            border-radius: 50px;
            padding: 1rem 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 1rem;
        }

        .messages-pill-left {
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .messages-icon-wrapper {
            position: relative;
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #ff6b9d, #ff8fab);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
        }

        .messages-badge {
            position: absolute;
            top: -4px;
            right: -4px;
            background: #ef4444;
            color: white;
            font-size: 0.7rem;
            font-weight: 700;
            width: 20px;
            height: 20px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px solid #0f0f12;
        }

        .messages-pill-title {
            font-size: 1.1rem;
            font-weight: 600;
        }

        .messages-pill-title a {
            color: white;
            text-decoration: none;
            transition: opacity 0.2s ease;
        }

        .messages-pill-title a:hover {
            opacity: 0.8;
        }

        .messages-pill-right {
            display: flex;
            align-items: center;
            gap: 0rem;
            padding-left: 0.5rem;
        }

        .pill-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            border: 2px solid #0f0f12;
            margin-left: -8px;
            transition: transform 0.2s ease;
        }

        .pill-avatar:first-child {
            margin-left: 0;
        }

        .pill-avatar:hover {
            transform: translateY(-2px);
            z-index: 10;
        }

        .pill-more {
            color: #9ca3af;
            font-size: 1.2rem;
            cursor: pointer;
            margin-left: 0.5rem;
            text-decoration: none;
            transition: color 0.2s ease;
        }

        .pill-more:hover {
            color: #fff;
        }

        .card-title {
            font-weight: 600;
            font-size: 1rem;
            margin-bottom: 1rem;
        }

        /* Matches Card */
        .matches-card {
            padding: 1.5rem;
            flex: 1;
            display: flex;
            flex-direction: column;
            min-height: 0;
        }

        .matches-list {
            flex: 1;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
            scrollbar-width: none;
            -ms-overflow-style: none;
        }

        .match-item {
            display: flex;
            gap: 0.75rem;
            padding: 0.75rem;
            border-radius: 12px;
            cursor: pointer;
            transition: background 0.2s ease;
        }

        .match-item:hover {
            background: rgba(255, 255, 255, 0.05);
        }

        .match-avatar {
            position: relative;
            flex-shrink: 0;
        }

        .match-avatar img {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            object-fit: cover;
        }

        .match-avatar .online-indicator {
            position: absolute;
            bottom: 2px;
            right: 2px;
            width: 12px;
            height: 12px;
            background: #4ade80;
            border: 2px solid #0f0f12;
            border-radius: 50%;
        }

        .match-content {
            flex: 1;
            min-width: 0;
        }

        .match-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.25rem;
        }

        .match-name {
            font-weight: 600;
            font-size: 0.9rem;
        }

        .match-time {
            font-size: 0.75rem;
            color: #9ca3af;
        }

        .match-preview {
            font-size: 0.85rem;
            color: #9ca3af;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        /* Discover Feed Title */
        .feed-title {
            position: absolute;
            top: calc(50% - 360px);
            left: calc(50% - 210px);
            font-size: 2rem;
            font-weight: 700;
            z-index: 10;
        }

        /* Logout button in sidebar */
        .logout-btn {
            padding: 0.75rem 1.5rem;
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.3);
            color: #ef4444;
            text-decoration: none;
            border-radius: 12px;
            text-align: center;
            font-weight: 500;
            transition: all 0.2s ease;
            display: block;
        }

        .logout-btn:hover {
            background: rgba(239, 68, 68, 0.2);
            transform: scale(1.02);
        }
    </style>
</head>
<body>
    <!-- Background Animation -->
    <div class="bg-image"></div>
    
    <!-- Ambient Blobs -->
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <!-- Main Container -->
    <div class="container">
        <!-- LEFT PANEL -->
        <div class="left-panel">
            <!-- Logo -->
            <div class="logo glass">
                <span class="logo-icon">💘</span>
                <span>Soulmate</span>
            </div>

            <!-- Feed Title -->
            <div class="feed-title">Discover</div>

            <!-- Card Stack -->
            <div class="card-stack">
                <!-- Background Cards -->
                <div class="swipe-card back-2 glass"></div>
                <div class="swipe-card back-1 glass"></div>
                
                <!-- Main Card -->
                <div class="swipe-card main" id="mainCard">
                    <div style="width: 100%; height: 100%; background: linear-gradient(135deg, #ff6b9d, #a855f7); display: flex; align-items: center; justify-content: center; font-size: 15rem; font-weight: 700; color: white;">K</div>
                    <div class="card-overlay">
                        <div class="card-name">Kisa, 24</div>
                        <div class="card-bio">Loves hiking, coffee, and good conversation. ENFJ.</div>
                        <div class="card-status">
                            <span class="status-dot"></span>
                            <span>Active now</span>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="actions">
                    <button class="action-btn pass" onclick="swipeCard('left')">✕</button>
                    <button class="action-btn star" onclick="superLike()">★</button>
                    <button class="action-btn like" onclick="swipeCard('right')">❤️</button>
                </div>
            </div>
        </div>

        <!-- RIGHT PANEL -->
        <div class="right-panel">
            <!-- Profile Card -->
            <div class="profile-card glass">
                <a href="${pageContext.request.contextPath}/profile" style="text-decoration: none;">
                    <div class="profile-avatar">
                        <div class="avatar-img"><%= firstName.substring(0, 1).toUpperCase() %></div>
                    </div>
                </a>
                <div class="profile-info">
                    <div class="profile-name"><%= firstName %> <%= lastName %></div>
                    <div class="profile-subtitle"><%= username %></div>
                </div>
                <div class="verification-badge <%= emailVerified ? "verified" : "not-verified" %>">
                    <%= emailVerified ? "✓" : "✕" %>
                </div>
            </div>

            <!-- Messages Pill Header -->
            <div class="messages-pill glass">
                <div class="messages-pill-left">
                    <div class="messages-icon-wrapper">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat-left-heart-fill" viewBox="0 0 16 16">
                        <path d="M2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm6 3.993c1.664-1.711 5.825 1.283 0 5.132-5.825-3.85-1.664-6.843 0-5.132"/>
                        </svg>
                        <c:if test="${unreadMessageCount > 0}">
                            <span class="messages-badge">${unreadMessageCount}</span>
                        </c:if>
                    </div>
                    <span class="messages-pill-title"><a href="${pageContext.request.contextPath}/messages">Messages</a></span>
                </div>
                <div class="messages-pill-right">
                    <c:choose>
                        <c:when test="${not empty recentConversations}">
                            <c:forEach var="conversation" items="${recentConversations}" varStatus="status">
                                <div class="pill-avatar" style="background: linear-gradient(135deg, 
                                    <c:choose>
                                        <c:when test="${status.index == 0}">#ff6b9d, #a855f7</c:when>
                                        <c:when test="${status.index == 1}">#a855f7, #7c3aed</c:when>
                                        <c:otherwise>#ff8fab, #ff6b9d</c:otherwise>
                                    </c:choose>
                                    ); color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 0.8rem;">${conversation.firstName.substring(0, 1).toUpperCase()}</div>
                            </c:forEach>
                            <c:if test="${totalConversationCount > 3}">
                                <a href="${pageContext.request.contextPath}/messages" class="pill-more">⋯</a>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <div class="pill-avatar" style="background: rgba(255, 255, 255, 0.1); color: #9ca3af; display: flex; align-items: center; justify-content: center; font-size: 0.7rem;">💬</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Matches Card -->
            <div class="matches-card glass">
                <div class="card-title">Matches</div>
                <div class="matches-list">
                    <c:choose>
                        <c:when test="${not empty matchedUsers}">
                            <c:forEach var="match" items="${matchedUsers}" varStatus="status">
                                <div class="match-item">
                                    <div class="match-avatar" style="background: linear-gradient(135deg, 
                                        <c:choose>
                                            <c:when test="${status.index % 4 == 0}">#ff6b9d, #a855f7</c:when>
                                            <c:when test="${status.index % 4 == 1}">#a855f7, #7c3aed</c:when>
                                            <c:when test="${status.index % 4 == 2}">#ff8fab, #ff6b9d</c:when>
                                            <c:otherwise>#a855f7, #ff6b9d</c:otherwise>
                                        </c:choose>
                                        ); color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 1.2rem; width: 48px; height: 48px; border-radius: 50%;">
                                        ${match.firstName.substring(0, 1).toUpperCase()}
                                        <c:if test="${status.index == 0 || status.index == 4}">
                                            <span class="online-indicator"></span>
                                        </c:if>
                                    </div>
                                    <div class="match-content">
                                        <div class="match-header">
                                            <span class="match-name">${match.firstName}</span>
                                            <span class="match-time">
                                                <c:choose>
                                                    <c:when test="${status.index == 0}">1:58 AM</c:when>
                                                    <c:when test="${status.index == 1}">11:20 PM</c:when>
                                                    <c:when test="${status.index == 2}">3:24 AM</c:when>
                                                    <c:when test="${status.index == 3}">5:30 PM</c:when>
                                                    <c:when test="${status.index == 4}">3:24 PM</c:when>
                                                    <c:otherwise>2:15 PM</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="match-preview">
                                            <c:choose>
                                                <c:when test="${status.index == 0}">Matched today 💕</c:when>
                                                <c:when test="${status.index == 1}">Matched yesterday ✨</c:when>
                                                <c:when test="${status.index == 2}">New match 🎉</c:when>
                                                <c:when test="${status.index == 3}">Matched today 💕</c:when>
                                                <c:when test="${status.index == 4}">New match 🔥</c:when>
                                                <c:otherwise>Matched today ✨</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; padding: 2rem; color: #9ca3af;">
                                <p>No matches yet</p>
                                <p style="font-size: 0.85rem; margin-top: 0.5rem;">Start swiping to find your soulmate! 💕</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Logout Button -->
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn glass">Logout</a>
        </div>
    </div>

    <script>
        // Swipe Card Interaction
        const mainCard = document.getElementById('mainCard');
        let isDragging = false;
        let startX = 0;
        let currentX = 0;

        mainCard.addEventListener('mousedown', (e) => {
            isDragging = true;
            startX = e.clientX;
            mainCard.style.cursor = 'grabbing';
        });

        document.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            currentX = e.clientX - startX;
            const rotation = currentX / 20;
            mainCard.style.transform = `translateX(${currentX}px) rotate(${rotation}deg)`;
            
            // Color overlay based on direction
            if (currentX > 50) {
                mainCard.style.borderColor = 'rgba(74, 222, 128, 0.5)';
            } else if (currentX < -50) {
                mainCard.style.borderColor = 'rgba(239, 68, 68, 0.5)';
            } else {
                mainCard.style.borderColor = 'transparent';
            }
        });

        document.addEventListener('mouseup', () => {
            if (!isDragging) return;
            isDragging = false;
            mainCard.style.cursor = 'grab';
            
            if (Math.abs(currentX) > 150) {
                // Swipe completed
                const direction = currentX > 0 ? 'right' : 'left';
                swipeCard(direction);
            } else {
                // Return to center
                mainCard.style.transform = '';
                mainCard.style.borderColor = 'transparent';
            }
            currentX = 0;
        });

        function swipeCard(direction) {
            const distance = direction === 'right' ? 1000 : -1000;
            mainCard.style.transition = 'transform 0.5s ease, opacity 0.5s ease';
            mainCard.style.transform = `translateX(${distance}px) rotate(${distance / 20}deg)`;
            mainCard.style.opacity = '0';
            
            setTimeout(() => {
                // Reset card
                mainCard.style.transition = 'none';
                mainCard.style.transform = '';
                mainCard.style.opacity = '1';
                mainCard.style.borderColor = 'transparent';
                
                // In a real app, load next profile here
                setTimeout(() => {
                    mainCard.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
                }, 50);
            }, 500);
        }

        function superLike() {
            mainCard.style.transition = 'transform 0.5s ease, opacity 0.5s ease';
            mainCard.style.transform = 'translateY(-1000px) scale(0.8)';
            mainCard.style.opacity = '0';
            
            setTimeout(() => {
                mainCard.style.transition = 'none';
                mainCard.style.transform = '';
                mainCard.style.opacity = '1';
                
                setTimeout(() => {
                    mainCard.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
                }, 50);
            }, 500);
        }

        // Active states for navigation
        document.querySelectorAll('.match-item').forEach(item => {
            item.addEventListener('click', function() {
                document.querySelectorAll('.match-item').forEach(i => {
                    i.style.background = '';
                });
                this.style.background = 'rgba(255, 255, 255, 0.08)';
            });
        });
    </script>
</body>
</html>
