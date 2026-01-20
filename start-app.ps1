# Dating Platform - Quick Start Script
# Run this script to start the entire application

param(
    [switch]$Rebuild = $false,
    [switch]$Clean = $false
)

# Configuration
$JAVA_HOME = "C:\Program Files\Java\jdk-25"
$MAVEN_HOME = "C:\Users\gaura\OneDrive\Desktop\Projects\Installations\apache-maven-3.9.12-bin\apache-maven-3.9.12"
$CATALINA_HOME = "C:\Users\gaura\OneDrive\Desktop\Projects\Installations\apache-tomcat-10.1.50"
$PROJECT_DIR = "C:\Users\gaura\OneDrive\Desktop\Projects\Dating-Platform"
$WAR_FILE = "$PROJECT_DIR\target\web-project.war"
$APP_URL = "http://localhost:8080/web-project/login"

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   Dating Platform - Quick Start" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Set environment variables
$env:JAVA_HOME = $JAVA_HOME
$env:MAVEN_HOME = $MAVEN_HOME
$env:CATALINA_HOME = $CATALINA_HOME
$env:Path = "$MAVEN_HOME\bin;$env:Path"

# Step 1: Check MySQL
Write-Host "[1/6] Checking MySQL service..." -ForegroundColor Yellow
$mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
if ($mysqlService.Status -ne "Running") {
    Write-Host "   MySQL is not running. Please start MySQL80 service." -ForegroundColor Red
    exit 1
}
Write-Host "   MySQL is running" -ForegroundColor Green

# Step 2: Stop existing Tomcat
Write-Host "[2/6] Stopping existing Tomcat instance..." -ForegroundColor Yellow
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2
Write-Host "   Tomcat stopped" -ForegroundColor Green

# Step 3: Clean deployment (if requested)
if ($Clean) {
    Write-Host "[3/6] Cleaning old deployment..." -ForegroundColor Yellow
    Remove-Item "$CATALINA_HOME\webapps\web-project" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item "$CATALINA_HOME\webapps\web-project.war" -Force -ErrorAction SilentlyContinue
    Write-Host "   Old deployment cleaned" -ForegroundColor Green
} else {
    Write-Host "[3/6] Skipping clean (use -Clean to clean old deployment)" -ForegroundColor Gray
}

# Step 4: Build project (if requested or WAR doesn't exist)
if ($Rebuild -or -not (Test-Path $WAR_FILE)) {
    Write-Host "[4/6] Building project with Maven..." -ForegroundColor Yellow
    Set-Location $PROJECT_DIR
    mvn clean package -DskipTests -q
    if ($LASTEXITCODE -ne 0) {
        Write-Host "   Build failed!" -ForegroundColor Red
        exit 1
    }
    Write-Host "   Build successful" -ForegroundColor Green
} else {
    Write-Host "[4/6] Skipping build (use -Rebuild to force rebuild)" -ForegroundColor Gray
}

# Step 5: Deploy WAR file
Write-Host "[5/6] Deploying to Tomcat..." -ForegroundColor Yellow
Copy-Item $WAR_FILE -Destination "$CATALINA_HOME\webapps\" -Force
Write-Host "   WAR deployed to Tomcat" -ForegroundColor Green

# Step 6: Start Tomcat
Write-Host "[6/6] Starting Tomcat..." -ForegroundColor Yellow
& "$CATALINA_HOME\bin\startup.bat"
Start-Sleep -Seconds 8

# Verify Tomcat is running
$tomcatProcess = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($tomcatProcess) {
    Write-Host "   Tomcat started (PID: $($tomcatProcess.Id))" -ForegroundColor Green
} else {
    Write-Host "   Tomcat failed to start!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Green
Write-Host "   SUCCESS! Dating Platform is READY!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Opening application in browser..." -ForegroundColor Cyan
Write-Host "URL: $APP_URL" -ForegroundColor Cyan
Write-Host ""
Write-Host "Test Login:" -ForegroundColor Yellow
Write-Host "   Email: john@doe.com" -ForegroundColor White
Write-Host "   Password: Admin@123" -ForegroundColor White
Write-Host ""
Write-Host "Tomcat Manager: http://localhost:8080/manager" -ForegroundColor Gray
Write-Host "Server Status: http://localhost:8080" -ForegroundColor Gray
Write-Host ""

# Open browser
Start-Sleep -Seconds 2
Start-Process $APP_URL

Write-Host "Press Ctrl+C to stop Tomcat when done" -ForegroundColor Yellow
Write-Host ""
