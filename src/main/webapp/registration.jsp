<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registration Page</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
  <style>
    body {
      font-family: Arial, sans-serif;
      background: linear-gradient(to bottom, #98c1d9, #e0fbfc);
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .container {
      background-color: #3d5a80;
      color: #ffffff;
      padding: 30px 40px;
      border-radius: 20px;
      width: 400px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    }

    h3 {
      text-align: center;
      margin-bottom: 20px;
      font-size: 1.8em;
    }

    .form-group {
      margin-bottom: 15px;
    }

    label {
      font-size: 1em;
      margin-bottom: 5px;
      color: #ffffff;
    }

    input, select {
      padding: 10px;
      border: 1px solid #98c1d9;
      border-radius: 20px;
      background-color: #e0fbfc;
      color: #293241;
      font-size: 1em;
    }

    input:focus, select:focus {
      border: 2px solid #ee6c4d;
      outline: none;
      background-color: #ffffff;
    }

    .signup-button {
      background-color: #ee6c4d;
      color: #fff;
      font-weight: bold;
      border: none;
      border-radius: 25px;
      padding: 15px 30px;
      font-size: 1.2em;
      width: auto;
      margin: 0 auto;
      display: block;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    .signup-button:hover {
      background-color: #ffffff;
      color: #293241;
    }
  </style>
</head>
<body>
  <div class="container">
    <h3>User Registration</h3>
    <form id="signup-form" action="login.jsp" method="POST">
      <div class="form-group">
        <label for="userId">User ID</label>
        <input type="text" class="form-control" id="userId" name="userId" placeholder="Enter your User ID" required>
      </div>
      <div class="form-group">
        <label for="firstName">First Name</label>
        <input type="text" class="form-control" id="firstName" name="firstName" placeholder="Enter your First Name" required>
      </div>
      <div class="form-group">
        <label for="lastName">Last Name</label>
        <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Enter your Last Name" required>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="Enter your Email" required>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Enter your Password" required>
      </div>
      <div class="form-group">
        <label for="dateOfBirth">Date of Birth</label>
        <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" required>
      </div>
      <div class="form-group">
        <label for="gender">Gender</label>
        <select class="form-control" id="gender" name="gender" required>
          <option value="">Select Gender</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>
      </div>
      <input type="hidden" id="registrationDate" name="registrationDate">
      <button type="submit" class="signup-button">Sign Up</button>
    </form>
  </div>
  <script>
    // Automatically set registration date to today's date
    document.getElementById('registrationDate').value = new Date().toISOString().split('T')[0];
  </script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
</body>
</html>
