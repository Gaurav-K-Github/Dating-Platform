<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - Dating Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-4">
        <h2>My Profile</h2>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <form method="post" action="profile">
            <div class="row">
                <div class="col-md-8">
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5>Profile Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="bio" class="form-label">Bio</label>
                                <textarea class="form-control" id="bio" name="bio" rows="4" 
                                          maxlength="500" placeholder="Tell us about yourself...">${profile.bio}</textarea>
                                <small class="text-muted">Maximum 500 characters</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="location" class="form-label">Location</label>
                                <input type="text" class="form-control" id="location" name="location" 
                                       value="${profile.location}" placeholder="City, State">
                            </div>
                        </div>
                    </div>
                    
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5>Interests</h5>
                        </div>
                        <div class="card-body">
                            <p class="text-muted">Select your interests (choose multiple)</p>
                            <div class="row">
                                <c:forEach items="${allInterests}" var="interest">
                                    <div class="col-md-4 mb-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" 
                                                   name="interests" value="${interest.interestId}" 
                                                   id="interest${interest.interestId}"
                                                   <c:if test="${userInterestIds.contains(interest.interestId)}">checked</c:if>>
                                            <label class="form-check-label" for="interest${interest.interestId}">
                                                ${interest.interestName}
                                            </label>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Matching Preferences</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="preferredGender" class="form-label">Looking for</label>
                                <select class="form-select" id="preferredGender" name="preferredGender">
                                    <option value="Any" <c:if test="${preferences.preferredGender == 'Any'}">selected</c:if>>Anyone</option>
                                    <option value="Male" <c:if test="${preferences.preferredGender == 'Male'}">selected</c:if>>Men</option>
                                    <option value="Female" <c:if test="${preferences.preferredGender == 'Female'}">selected</c:if>>Women</option>
                                    <option value="Other" <c:if test="${preferences.preferredGender == 'Other'}">selected</c:if>>Other</option>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="minAge" class="form-label">Age Range</label>
                                <div class="row">
                                    <div class="col-6">
                                        <input type="number" class="form-control" id="minAge" name="minAge" 
                                               value="${preferences.minAge}" min="18" max="100" placeholder="Min">
                                    </div>
                                    <div class="col-6">
                                        <input type="number" class="form-control" id="maxAge" name="maxAge" 
                                               value="${preferences.maxAge}" min="18" max="100" placeholder="Max">
                                    </div>
                                </div>
                                <small class="text-muted">Age ${preferences.minAge} to ${preferences.maxAge}</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="mb-3">
                <button type="submit" class="btn btn-primary btn-lg">Save Profile</button>
                <a href="dashboard" class="btn btn-secondary btn-lg">Cancel</a>
            </div>
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
