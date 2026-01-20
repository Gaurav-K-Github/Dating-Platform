# Dating Platform - Stop Script
# Run this script to stop the application

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   Stopping Dating Platform" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Stop Tomcat
Write-Host "Stopping Tomcat..." -ForegroundColor Yellow
$tomcatProcess = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($tomcatProcess) {
    Stop-Process -Name "java" -Force
    Write-Host "Tomcat stopped (PID: $($tomcatProcess.Id))" -ForegroundColor Green
} else {
    Write-Host "Tomcat is not running" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Application stopped successfully" -ForegroundColor Green
Write-Host ""
