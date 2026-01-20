# Complete Database Reset Script
param(
    [string]$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
)

Write-Host "================================================"
Write-Host "   COMPLETE DATABASE RESET"
Write-Host "================================================"
Write-Host ""
Write-Host "This will completely reset the database with fresh test data."
Write-Host ""

# Read password securely
$securePassword = Read-Host "Enter MySQL root password" -AsSecureString
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToCoTaskMemUnicode($securePassword))

Write-Host ""
Write-Host "Executing database reset..."
Write-Host ""

# Execute the SQL script
$sqlFile = "$PSScriptRoot\reset-db.sql"

if (Test-Path $sqlFile) {
    Get-Content $sqlFile | & $mysqlPath -u root -p"$plainPassword" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "================================================"
        Write-Host "   SUCCESS! Database reset complete."
        Write-Host "================================================"
        Write-Host ""
        Write-Host "Test Accounts:"
        Write-Host "  Email: john@doe.com"
        Write-Host "  Email: sophia@example.com"
        Write-Host "  Email: ava@example.com"
        Write-Host ""
        Write-Host "Password for all: Admin@123"
        Write-Host ""
    } else {
        Write-Host "Error executing SQL script"
    }
} else {
    Write-Host "Error: reset-db.sql not found at $sqlFile"
}
