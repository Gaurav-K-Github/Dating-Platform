<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dating Dashboard</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    /* General Styles */
    body {
      font-family: 'Arial', sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f5f5f5;
    }

    .dashboard {
      display: flex;
      width: 100%;
      height: 100vh;
    }

    /* Sidebar */
    .sidebar {
      width: 250px;
      background-color: #3d5a80;
      color: #ffffff;
      padding: 20px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    .sidebar h2 {
      font-size: 24px;
      margin-bottom: 20px;
    }

    .sidebar nav a {
      text-decoration: none;
      color: #ffffff;
      padding: 10px 15px;
      display: block;
      border-radius: 5px;
      margin-bottom: 10px;
    }

    .sidebar nav a.active {
      background-color: #4c91d9;
    }

    .sidebar nav a:hover {
      background-color: #5a9be0;
    }

    .sidebar .support a {
      color: #ffffff;
      text-decoration: none;
      margin-bottom: 10px;
      display: block;
    }

    /* Main Content */
    .main-content {
      flex-grow: 1;
      background-color: #e0fbfc;
      padding: 20px;
      overflow-y: auto;
    }

    header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    header input {
      padding: 10px;
      border: none;
      border-radius: 20px;
      flex-grow: 1;
      max-width: 400px;
      margin-right: 10px;
    }

    header .upgrade-btn {
      background-color: #ee6c4d;
      color: #ffffff;
      padding: 10px 20px;
      border: none;
      border-radius: 20px;
      cursor: pointer;
    }

    header .profile {
      color: #3d5a80;
      font-size: 18px;
    }

    /* Profile Carousel */
    .profile-carousel {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
    }

    .profile-carousel .profiles {
      display: flex;
      gap: 15px;
      overflow: auto;
    }

    .profile-carousel .profile-card {
      background-color: #ffffff;
      padding: 10px;
      border-radius: 10px;
      color: #3d5a80;
      text-align: center;
      width: 150px;
      box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
    }

    .profile-card img {
      width: 100%;
      border-radius: 10px;
      margin-bottom: 10px;
    }

    /* Visitors Chart Placeholder */
    .visitors p {
      height: 150px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #98c1d9;
      background-color: #f0f0f0;
      border-radius: 8px;
    }

    /* Content Grid */
    .content-grid {
      display: grid;
      grid-template-columns: 2fr 2fr 1fr;
      gap: 20px;
    }

    .activity, .visitors, .suggestions {
      background-color: #ffffff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body>
  <div class="dashboard">
    <!-- Sidebar -->
    <div class="sidebar">
      <h2>Soulmate</h2>
      <nav>
        <a href="#" class="active">Dashboard</a>
        <a href="#">Matches</a>
        <a href="#">New & Online</a>
      </nav>
      <div class="support">
        <a href="#">Settings</a>
        <a href="#">Help</a>
        <a href="#">Logout</a>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Header -->
      <header>
        <input type="text" placeholder="Search here">
        <button class="upgrade-btn">Upgrade Now</button>
        <div class="profile">Hi, Mansural</div>
      </header>

      <!-- Profile Carousel -->
      <div class="profile-carousel">
        <div class="d-flex gap-3 overflow-auto">
          <div class="profile-card">
            <img src="profile1.jpg" alt="Sharlin">
            <h6>Sharlin, 25</h6>
          </div>
          <div class="profile-card">
            <img src="profile2.jpg" alt="Jenifer">
            <h6>Jenifer, 23</h6>
          </div>
          <div class="profile-card">
            <img src="profile3.jpg" alt="Lusi">
            <h6>Lusi, 21</h6>
          </div>
        </div>
      </div>

      <!-- Activity, Visitors, and Suggestions -->
      <div class="content-grid">
        <!-- Activity -->
        <div class="activity">
          <h4>Activity</h4>
          <ul>
            <li>Naomi Campbell is now following you - 2 hours ago</li>
            <li>Jeena sent you a message - 4 hours ago</li>
            <li>Harshita matched 90% with you - 8 hours ago</li>
          </ul>
        </div>

        <!-- Visitors -->
        <div class="visitors">
          <h4>Visitors</h4>
          <p>Graph of Visitors for Last 7 Days</p>
        </div>

        <!-- Suggestions -->
        <div class="suggestions">
          <h4>You May Like</h4>
          <ul>
            <li>Cristina Albert, 26</li>
            <li>Helen Cole, 24</li>
            <li>Rihana Morgan, 24</li>
            <li>Harprit Kaur, 27</li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <!-- Bootstrap JS Bundle -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    document.querySelector(".upgrade-btn").addEventListener("click", () => {
      alert("Upgrade feature is coming soon!");
    });

    document.querySelectorAll(".sidebar nav a").forEach(link => {
      link.addEventListener("click", function () {
        document.querySelectorAll(".sidebar nav a").forEach(l => l.classList.remove("active"));
        this.classList.add("active");
      });
    });
  </script>
</body>
</html>
