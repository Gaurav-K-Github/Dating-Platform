# 💕 Dating Platform - Java Full-Stack Project

A modern, feature-rich online dating platform built with Java Servlets, JSP, and MySQL. This project demonstrates professional full-stack development practices including secure authentication, compatibility-based matching algorithms, real-time messaging, and contemporary UI/UX design.

## 🚀 Quick Start

```powershell
# Clone the repository
git clone https://github.com/your-username/dating-platform.git
cd dating-platform

# Start the application (automated script handles everything)
.\start-app.ps1
```

**That's it!** The browser will automatically open to the login page.

**Test Account:**
- Email: `john@doe.com`
- Password: `Admin@123`

---

## ✨ Features

### 🔐 Authentication & Security
- User registration with comprehensive validation
- Secure login with BCrypt password hashing (12 rounds)
- Session management with automatic timeout
- Email verification system (infrastructure ready)
- XSS prevention and SQL injection protection

### 👤 Profile Management
- Detailed user profiles (bio, location, education, job, interests)
- Multi-select interests from 10+ categories
- Profile completeness tracking
- View other users' profiles
- Edit and update profile information

### 💕 Smart Matching Engine
- **Compatibility scoring algorithm** (0-100%)
  - Age compatibility (20% weight)
  - Location proximity (15% weight)
  - Shared interests (50% weight)
  - Gender preferences (15% weight)
- Advanced filtering by age range, distance, gender, interests
- Match ranking and recommendations
- Mutual match detection

### 💬 Real-Time Messaging
- Direct messaging between matched users
- Conversation history with timestamps
- Unread message badges
- Recent conversations panel
- Clean chat interface

### 🎨 Modern UI/UX (2026 Design)
- Glassmorphism design language
- Animated gradient backgrounds
- Tinder-style card stack interface
- Dark theme with purple/blue palette
- Responsive design with Inter font family
- SVG icons for social platforms

---

## 🛠️ Technologies Used

### Backend
- **Java 17+** - Core programming language
- **Jakarta EE 5.0** - Servlet API
- **Apache Tomcat 10.1.50** - Application server
- **Maven 3.9.12** - Dependency management & build tool

### Database
- **MySQL 8.0** - Primary database
- Normalized schema with foreign key constraints
- PreparedStatements for SQL injection protection

### Frontend
- **JSP** - Server-side rendering
- **JSTL** - Tag libraries for dynamic content
- **HTML5/CSS3** - Modern web standards
- **JavaScript (Vanilla)** - Client-side interactivity
- **Bootstrap Icons** - SVG icon library

### Security
- **BCrypt** - Password hashing
- **Session-based authentication**
- **Input validation** (server-side)
- **HTTPS-ready** (security headers configured)

---

## 📋 System Requirements

- **Java:** JDK 17 or higher
- **Maven:** 3.6+
- **MySQL:** 8.0+
- **Apache Tomcat:** 10.1+
- **OS:** Windows (PowerShell scripts), Linux/Mac (adaptable)

---

## 📦 Installation & Setup

### Prerequisites
1. Install Java JDK 17+
2. Install MySQL 8.0
3. Install Apache Maven
4. Install Apache Tomcat 10.1.50

### Step 1: Database Setup
```powershell
# Run the database setup script
.\setup-database.ps1
```
This creates the `dating_platform` database and all required tables.

### Step 2: Configure Environment
Update database credentials if needed (default: root/root):
```java
// src/main/javaproj/com/dating/dao/UserDAO.java
private static final String DB_URL = "jdbc:mysql://localhost:3306/dating_platform";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "root";
```

### Step 3: Build & Deploy
```powershell
# Build with Maven
mvn clean package

# Deploy to Tomcat (automatic)
.\start-app.ps1 -Rebuild
```

### Step 4: Access Application
- **Login:** http://localhost:8080/web-project/login
- **Dashboard:** http://localhost:8080/web-project/dashboard
- **Messages:** http://localhost:8080/web-project/messages

---

## 📁 Project Structure

```
dating-platform/
├── src/
│   ├── main/
│   │   ├── javaproj/com/dating/
│   │   │   ├── controller/        # Servlets (MVC Controllers)
│   │   │   ├── dao/               # Data Access Objects
│   │   │   ├── model/             # Entity classes
│   │   │   ├── service/           # Business logic
│   │   │   └── util/              # Utilities (Validation, Tokens)
│   │   └── webapp/
│   │       ├── *.jsp              # View pages
│   │       └── WEB-INF/
│   │           └── web.xml        # Servlet mappings
├── pom.xml                        # Maven configuration
├── Schema.sql                     # Database schema
├── start-app.ps1                  # Quick start script
└── ROADMAP.md                     # Development roadmap
```

---

## 🎯 Key Architecture Patterns

### MVC (Model-View-Controller)
- **Model:** POJOs in `com.dating.model`
- **View:** JSP pages in `webapp/`
- **Controller:** Servlets in `com.dating.controller`

### DAO (Data Access Object)
Separates database operations from business logic:
```java
UserDAO → Database operations
MatchDAO → Matching queries
MessageDAO → Messaging queries
```

### Service Layer
Encapsulates complex business logic:
```java
MatchingService → Compatibility algorithm
EmailService → Email sending (SMTP)
```

---

## 🔒 Security Features

1. **Password Security:** BCrypt with 12 rounds (2^12 iterations)
2. **SQL Injection Prevention:** PreparedStatements for all queries
3. **XSS Protection:** Input sanitization and output encoding
4. **Session Security:** 30-minute timeout, HttpOnly cookies
5. **CSRF Protection:** (Planned - use tokens for forms)
6. **Security Headers:** X-Content-Type-Options, X-Frame-Options, X-XSS-Protection

---

## 🧪 Testing

### Manual Testing
Use the test account or create a new one:
```
Email: john@doe.com
Password: Admin@123
```

### Load Test Data
```powershell
# Populate database with 50 sample users
mysql -u root -p dating_platform < test-data-50users.sql
```

### Unit Tests (TODO)
```bash
mvn test
```

---

## 📊 Database Schema

### Core Tables
- `users` - Authentication and basic info
- `profiles` - Detailed user profiles
- `preferences` - Matching preferences
- `interests` - Interest categories
- `user_interests` - User-interest associations (junction table)
- `matches` - Compatibility scores
- `messages` - User messages
- `verification_tokens` - Email verification

See [Schema.sql](Schema.sql) for complete schema.

---

## 🗺️ Development Roadmap

See [ROADMAP.md](ROADMAP.md) for:
- ✅ Completed features
- 🚧 In-progress work
- 📝 Planned enhancements
- 🐛 Known issues
- 🎯 Interview talking points

### Next Features
1. Profile photo upload
2. Social login (Google, Apple, X)
3. Email delivery (SMTP integration)
4. Block/Report users
5. Notifications system

---

## 💡 Interview Talking Points

### Elevator Pitch
*"I built a full-stack dating platform using Java Servlets, JSP, and MySQL. It features secure authentication with BCrypt hashing, a compatibility-based matching algorithm, real-time messaging, and a modern glassmorphism UI. I implemented MVC architecture, DAO pattern, and security best practices including XSS prevention and SQL injection protection."*

### Technical Highlights
- **Security:** BCrypt (12 rounds), PreparedStatements, session management
- **Algorithm:** Multi-criteria compatibility scoring with weighted factors
- **Architecture:** Clean separation of concerns (MVC + DAO + Service)
- **Database:** Normalized schema with proper foreign key relationships
- **DevOps:** Automated PowerShell scripts for deployment

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Coding Standards
- Java: PascalCase for classes, camelCase for methods
- SQL: UPPERCASE keywords, snake_case tables
- JSP: Follow existing formatting conventions

---

## 📝 Scripts Reference

### start-app.ps1
```powershell
.\start-app.ps1              # Quick start (no rebuild)
.\start-app.ps1 -Rebuild     # Rebuild and deploy
.\start-app.ps1 -Clean       # Clean previous build
```

### stop-app.ps1
```powershell
.\stop-app.ps1               # Stop Tomcat server
```

### reset-database.ps1
```powershell
.\reset-database.ps1         # Drop and recreate database
```

---

## 📚 Resources

- [Jakarta EE Documentation](https://jakarta.ee/learn/)
- [MySQL Reference Manual](https://dev.mysql.com/doc/)
- [Apache Tomcat Guide](https://tomcat.apache.org/tomcat-10.1-doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Gaurav**  
*Full-Stack Java Developer*

---

## 🙏 Acknowledgments

- Bootstrap Icons for SVG assets
- Inter font family by Rasmus Andersson
- Modern design inspiration from leading dating platforms

---

**Built with ❤️ using Java**

</body>
</html>
