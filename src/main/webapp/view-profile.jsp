<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.time.LocalDate,java.time.Period" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${viewUser.firstName}'s Profile - Dating Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .profile-photo {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            background: #6c757d;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 72px;
            margin: 0 auto;
        }
        .interest-badge {
            display: inline-block;
            padding: 5px 15px;
            margin: 5px;
            background: #e9ecef;
            border-radius: 20px;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-4">
        <a href="javascript:history.back()" class="btn btn-outline-secondary mb-3">← Back</a>
        
        <%@ page import="java.time.LocalDate, java.time.Period" %>
        <%
            com.dating.model.User viewUser = (com.dating.model.User) request.getAttribute("viewUser");
            LocalDate birthDate = viewUser.getDateOfBirth();
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            pageContext.setAttribute("age", age);
        %>
        
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body text-center">
                        <div class="profile-photo mb-3">
                            ${viewUser.firstName.substring(0,1)}${viewUser.lastName.substring(0,1)}
                        </div>
                        <h2>${viewUser.firstName} ${viewUser.lastName}</h2>
                        <p class="text-muted">${age} years old</p>
                        <p class="text-muted">
                            <c:if test="${viewUser.gender == 'Male'}">♂</c:if>
                            <c:if test="${viewUser.gender == 'Female'}">♀</c:if>
                            ${viewUser.gender}
                        </p>
                    </div>
                </div>
            </div>
            
            <div class="col-md-8">
                <div class="card mb-3">
                    <div class="card-header">
                        <h5>About</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty viewProfile && not empty viewProfile.bio}">
                                <p>${viewProfile.bio}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">No bio provided</p>
                            </c:otherwise>
                        </c:choose>
                        
                        <c:if test="${not empty viewProfile && not empty viewProfile.location}">
                            <p><strong>Location:</strong> ${viewProfile.location}</p>
                        </c:if>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h5>Interests</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty viewInterests}">
                                <c:forEach items="${viewInterests}" var="interest">
                                    <span class="interest-badge">${interest.interestName}</span>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">No interests listed</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
