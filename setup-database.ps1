# Database Setup Script for Dating Platform
# This script sets up the complete database schema and test data

$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$database = "dating_platform"
$schemaFile = "C:/Users/gaura/OneDrive/Desktop/Projects/Dating-Platform/Schema.sql"
$testDataFile = "C:/Users/gaura/OneDrive/Desktop/Projects/Dating-Platform/test-data.sql"

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   Dating Platform - Database Setup" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Prompt for MySQL root password
$password = Read-Host "Enter MySQL root password" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host ""
Write-Host "[1/3] Creating database schema..." -ForegroundColor Yellow

# Execute Schema.sql
$schemaCommand = "SOURCE $schemaFile;"
$schemaCommand | & $mysqlPath -u root -p"$plainPassword" $database 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "   Schema created successfully" -ForegroundColor Green
} else {
    Write-Host "   Error creating schema" -ForegroundColor Red
    exit 1
}

Write-Host "[2/3] Inserting test data..." -ForegroundColor Yellow

# Execute test-data.sql
$testDataCommand = "SOURCE $testDataFile;"
$testDataCommand | & $mysqlPath -u root -p"$plainPassword" $database 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "   Test data inserted successfully" -ForegroundColor Green
} else {
    Write-Host "   Error inserting test data" -ForegroundColor Red
    exit 1
}

Write-Host "[3/3] Verifying database..." -ForegroundColor Yellow

# Verify tables
$verifyCommand = @"
SELECT 
    (SELECT COUNT(*) FROM users) as users_count,
    (SELECT COUNT(*) FROM profiles) as profiles_count,
    (SELECT COUNT(*) FROM interests) as interests_count,
    (SELECT COUNT(*) FROM matches) as matches_count,
    (SELECT COUNT(*) FROM messages) as messages_count;
"@

$verifyCommand | & $mysqlPath -u root -p"$plainPassword" $database -t 2>&1

Write-Host ""
Write-Host "================================================" -ForegroundColor Green
Write-Host "   Database Setup Complete!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Test Users (all use password: Admin@123):" -ForegroundColor Cyan
Write-Host "  - john@doe.com (existing user)" -ForegroundColor White
Write-Host "  - emma@example.com" -ForegroundColor White
Write-Host "  - james@example.com" -ForegroundColor White
Write-Host "  - sophia@example.com" -ForegroundColor White
Write-Host "  - michael@example.com" -ForegroundColor White
Write-Host "  - olivia@example.com" -ForegroundColor White
Write-Host ""
Write-Host "You can now test the dating platform!" -ForegroundColor Green
Write-Host "Visit: http://localhost:8080/web-project/login" -ForegroundColor Cyan
