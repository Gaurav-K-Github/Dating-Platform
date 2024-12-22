<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login & Signup</title> 
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<style>
    body {
      font-family: Arial, sans-serif;
      background: linear-gradient(to bottom, #ddf1fc, #88cbf2);
      margin: 0;
      padding: 0;
    }

    .icon-img {
      width: 30px;
      height: 30px;
    }

    .social-btn {
      width: 50px;
      height: 50px;
    }

    h2 {
      color: #293241;
    }

    .bg-danger {
      background-color: #ee6c4d !important;
    }

    .btn-light:hover {
      background-color: #293241;
      color: white;
    }

    form .form-control {
      background-color: #f1f5f8;
      border: 1px solid #cbd5e1;
    }

    form .form-control:focus {
      box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
      border-color: #007bff;
    }
</style>
</head>
<body>
	<div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="row w-75 shadow-lg rounded overflow-hidden">
      <!-- Sign In Section -->
      <div class="col-md-6 bg-light p-4">
        <h2 class="text-center mb-4">Sign In</h2>
        <p class="text-center">Sign in using E-Mail Address</p>
        <form id="signInForm">
          <div class="mb-3">
            <input type="email" class="form-control" id="signInEmail" placeholder="Email" required>
          </div>
          <div class="mb-3">
            <input type="password" class="form-control" id="signInPassword" placeholder="Password" required>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <a href="#" class="text-decoration-none text-secondary">Forgot your password?</a>
            <button type="submit" class="btn btn-primary">SIGN IN</button>
          </div>
        </form>
      </div>
      <!-- Create Account Section -->
      <div class="col-md-6 bg-danger text-white d-flex flex-column justify-content-center align-items-center p-4">
        <h2>Create Account!</h2>
        <p class="text-center mb-4">Sign up if you still don't have an account...</p>
        <button class="btn btn-light fw-bold" id="signUpBtn">SIGN UP</button>
      </div>
    </div>
  </div>

	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
		integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
		integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
		crossorigin="anonymous"></script>
	<script>
    // Sign In form submission event
    document.getElementById("signInForm").addEventListener("submit", function (e) {
      e.preventDefault(); // Prevent default form submission
      const email = document.getElementById("signInEmail").value;
      const password = document.getElementById("signInPassword").value;

      if (email && password) {
        alert(`Welcome back, ${email}!`);
        window.location.href = "Dashboard.jsp";
      } else {
        alert("Please fill in all fields.");
      }
    });

    // Sign Up button event
    document.getElementById("signUpBtn").addEventListener("click", function () {
      window.location.href = "registration.jsp";
    });
  </script>
</body>
</html>
