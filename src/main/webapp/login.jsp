<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - Soulmate</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #0f0f12;
            color: white;
            min-height: 100vh;
            overflow: hidden;
            position: relative;
        }

        /* Background Image */
        .bg-image {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #4facfe 75%, #00f2fe 100%);
            background-size: 400% 400%;
            animation: gradientShift 15s ease infinite;
            filter: blur(80px);
            opacity: 0.6;
            z-index: 0;
        }

        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }

        /* Ambient Blobs */
        .blob {
            position: fixed;
            border-radius: 50%;
            filter: blur(80px);
            opacity: 0.5;
            animation: float 20s ease-in-out infinite;
            z-index: 0;
        }

        .blob-1 {
            width: 500px;
            height: 500px;
            background: radial-gradient(circle, #ff6b9d, transparent);
            top: -10%;
            left: -10%;
        }

        .blob-2 {
            width: 400px;
            height: 400px;
            background: radial-gradient(circle, #a855f7, transparent);
            bottom: -10%;
            right: -10%;
            animation-delay: -5s;
        }

        .blob-3 {
            width: 350px;
            height: 350px;
            background: radial-gradient(circle, #3b82f6, transparent);
            top: 50%;
            left: 50%;
            animation-delay: -10s;
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

        /* Logo */
        .logo {
            position: fixed;
            top: 2rem;
            left: 2rem;
            z-index: 100;
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 100px;
            padding: 0.75rem 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-weight: 600;
            font-size: 1.1rem;
        }

        .logo-icon {
            font-size: 1.5rem;
        }

        /* Main Container */
        .auth-container {
            position: relative;
            z-index: 1;
            display: flex;
            min-height: 100vh;
            align-items: center;
            justify-content: space-between;
            padding: 2rem;
            gap: 4rem;
        }

        /* Left Side - Hero Text */
        .hero-section {
            flex: 1;
            max-width: 600px;
            padding-left: 4rem;
        }

        .hero-title {
            font-size: 4.5rem;
            font-weight: 800;
            line-height: 1.1;
            margin-bottom: 1.5rem;
            background: linear-gradient(135deg, #ffffff 0%, #f0f0f0 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .hero-subtitle {
            font-size: 1.25rem;
            color: rgba(255, 255, 255, 0.7);
            font-weight: 400;
            line-height: 1.6;
        }

        /* Right Side - Auth Card */
        .auth-card {
            width: 100%;
            max-width: 440px;
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 32px;
            padding: 3rem;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        }

        .auth-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .auth-header h2 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
        }

        /* Toggle Switch */
        .auth-toggle {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .toggle-option {
            font-size: 0.95rem;
            color: rgba(255, 255, 255, 0.5);
            cursor: pointer;
            transition: color 0.3s ease;
            font-weight: 500;
        }

        .toggle-option.active {
            color: white;
        }

        .toggle-dot {
            width: 40px;
            height: 24px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 12px;
            position: relative;
            cursor: pointer;
        }

        .toggle-dot::after {
            content: '';
            position: absolute;
            width: 20px;
            height: 20px;
            background: white;
            border-radius: 50%;
            top: 2px;
            left: 2px;
            transition: transform 0.3s ease;
        }

        /* Form Styles */
        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 0.5rem;
            color: rgba(255, 255, 255, 0.9);
        }

        .form-control {
            width: 100%;
            padding: 1rem 1.25rem;
            background: rgba(255, 255, 255, 0.08);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 16px;
            color: white;
            font-size: 1rem;
            font-family: 'Inter', sans-serif;
            transition: all 0.3s ease;
            outline: none;
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        .form-control:focus {
            background: rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.3);
        }

        .btn-primary {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(135deg, #ff6b9d, #ff8fab);
            border: none;
            border-radius: 16px;
            color: white;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 0.5rem;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 24px rgba(255, 107, 157, 0.4);
        }

        /* Social Login */
        .social-divider {
            text-align: center;
            margin: 2rem 0 1.5rem;
            color: rgba(255, 255, 255, 0.5);
            font-size: 0.875rem;
        }

        .social-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
        }

        .social-btn {
            width: 56px;
            height: 56px;
            background: rgba(255, 255, 255, 0.08);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
            color: white;
            font-size: 1.5rem;
            text-decoration: none;
        }

        .social-btn:hover {
            background: rgba(255, 255, 255, 0.12);
            transform: translateY(-2px);
        }

        .social-btn img {
            display: block;
            height: 24px;
            width: 24px;
            object-fit: contain;
            filter: brightness(0) invert(1);
        }

        /* Bottom Link */
        .bottom-link {
            text-align: center;
            margin-top: 2rem;
            font-size: 0.95rem;
            color: rgba(255, 255, 255, 0.7);
        }

        .bottom-link a {
            color: #ff6b9d;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .bottom-link a:hover {
            color: #ff8fab;
        }

        /* Alert Messages */
        .alert {
            padding: 1rem 1.25rem;
            border-radius: 12px;
            margin-bottom: 1.5rem;
            font-size: 0.9rem;
        }

        .alert-success {
            background: rgba(74, 222, 128, 0.1);
            border: 1px solid rgba(74, 222, 128, 0.3);
            color: #4ade80;
        }

        .alert-danger {
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.3);
            color: #ef4444;
        }

        /* Coming Soon Popup */
        .popup-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            backdrop-filter: blur(5px);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
            animation: fadeIn 0.3s ease;
        }

        .popup-overlay.active {
            display: flex;
        }

        .popup-content {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 24px;
            padding: 2.5rem;
            max-width: 400px;
            text-align: center;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
            animation: popupSlide 0.3s ease;
        }

        .popup-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }

        .popup-title {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 0.75rem;
        }

        .popup-message {
            font-size: 1rem;
            color: rgba(255, 255, 255, 0.7);
            margin-bottom: 2rem;
        }

        .popup-close {
            width: 100%;
            padding: 0.875rem;
            background: linear-gradient(135deg, #ff6b9d, #ff8fab);
            border: none;
            border-radius: 12px;
            color: white;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .popup-close:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 24px rgba(255, 107, 157, 0.4);
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes popupSlide {
            from { transform: translateY(-20px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }

        /* Responsive */
        @media (max-width: 968px) {
            .auth-container {
                flex-direction: column;
                justify-content: center;
            }

            .hero-section {
                padding-left: 0;
                text-align: center;
            }

            .hero-title {
                font-size: 3rem;
            }
        }
    </style>
</head>
<body>
    <!-- Background -->
    <div class="bg-image"></div>
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <!-- Logo -->
    <div class="logo">
        <span class="logo-icon">💘</span>
        <span>Soulmate</span>
    </div>

    <!-- Main Container -->
    <div class="auth-container">
        <!-- Left Side - Hero Text -->
        <div class="hero-section">
            <h1 class="hero-title">Find someone who makes your soul glow.</h1>
            <p class="hero-subtitle">Join millions discovering meaningful connections in 2026.</p>
        </div>

        <!-- Right Side - Auth Card -->
        <div class="auth-card">
            <div class="auth-header">
                <h2>Welcome Back</h2>
            </div>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    ${successMessage}
                    <% session.removeAttribute("successMessage"); %>
                </div>
            </c:if>

            <c:if test="${not empty loginError}">
                <div class="alert alert-danger">
                    ${loginError}
                    <c:if test="${not empty loginErrorDetail}">
                        <br><small>${loginErrorDetail}</small>
                    </c:if>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/loginprocess" method="post">
                <div class="form-group">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn-primary">Continue</button>
            </form>

            <div class="social-divider">Or continue with</div>
            <div class="social-buttons">
                <a href="#" class="social-btn" onclick="showComingSoon(event)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
                        <path d="M15.545 6.558a9.4 9.4 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.7 7.7 0 0 1 5.352 2.082l-2.284 2.284A4.35 4.35 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.8 4.8 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.7 3.7 0 0 0 1.599-2.431H8v-3.08z"/>
                    </svg>
                </a>
                <a href="#" class="social-btn" onclick="showComingSoon(event)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
                        <path d="M11.182.008C11.148-.03 9.923.023 8.857 1.18c-1.066 1.156-.902 2.482-.878 2.516s1.52.087 2.475-1.258.762-2.391.728-2.43m3.314 11.733c-.048-.096-2.325-1.234-2.113-3.422s1.675-2.789 1.698-2.854-.597-.79-1.254-1.157a3.7 3.7 0 0 0-1.563-.434c-.108-.003-.483-.095-1.254.116-.508.139-1.653.589-1.968.607-.316.018-1.256-.522-2.267-.665-.647-.125-1.333.131-1.824.328-.49.196-1.422.754-2.074 2.237-.652 1.482-.311 3.83-.067 4.56s.625 1.924 1.273 2.796c.576.984 1.34 1.667 1.659 1.899s1.219.386 1.843.067c.502-.308 1.408-.485 1.766-.472.357.013 1.061.154 1.782.539.571.197 1.111.115 1.652-.105.541-.221 1.324-1.059 2.238-2.758q.52-1.185.473-1.282"/>
                    </svg>
                </a>
                <a href="#" class="social-btn" onclick="showComingSoon(event)">𝕏</a>
            </div>

            <div class="bottom-link">
                Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign up</a>
            </div>
        </div>
    </div>

    <!-- Coming Soon Popup -->
    <div class="popup-overlay" id="comingSoonPopup">
        <div class="popup-content">
            <div class="popup-icon">🚀</div>
            <div class="popup-title">Coming Soon!</div>
            <div class="popup-message">Social login features will be available in the next update. Stay tuned!</div>
            <button class="popup-close" onclick="closePopup()">Got it</button>
        </div>
    </div>

    <!-- Sparkle decoration -->
    <div class="sparkle">✦</div>

    <script>
        function showComingSoon(event) {
            event.preventDefault();
            document.getElementById('comingSoonPopup').classList.add('active');
        }

        function closePopup() {
            document.getElementById('comingSoonPopup').classList.remove('active');
        }

        // Close popup when clicking outside
        document.getElementById('comingSoonPopup').addEventListener('click', function(e) {
            if (e.target === this) {
                closePopup();
            }
        });
    </script>
</body>
</html>
