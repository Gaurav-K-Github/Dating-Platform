<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Email Verification - Dating Platform</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      font-family: Arial, sans-serif;
      background: linear-gradient(to bottom, #ddf1fc, #88cbf2);
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
    }

    .container {
      background-color: white;
      padding: 40px;
      border-radius: 20px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      max-width: 500px;
      text-align: center;
    }

    .success-icon {
      color: #28a745;
      font-size: 4em;
      margin-bottom: 20px;
    }

    .error-icon {
      color: #dc3545;
      font-size: 4em;
      margin-bottom: 20px;
    }

    h2 {
      color: #293241;
      margin-bottom: 20px;
    }

    .btn-primary {
      background-color: #ee6c4d;
      border: none;
      padding: 12px 30px;
      border-radius: 25px;
      font-weight: bold;
      margin-top: 20px;
    }

    .btn-primary:hover {
      background-color: #293241;
    }
  </style>
</head>
<body>
  <div class="container">
    <c:choose>
      <c:when test="${verificationSuccess}">
        <div class="success-icon">✓</div>
        <h2>Email Verified!</h2>
        <p class="lead">${message}</p>
        <a href="login" class="btn btn-primary">Go to Login</a>
      </c:when>
      <c:otherwise>
        <div class="error-icon">✗</div>
        <h2>Verification Failed</h2>
        <p class="lead">${verificationError}</p>
        <a href="login" class="btn btn-primary">Back to Login</a>
      </c:otherwise>
    </c:choose>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
