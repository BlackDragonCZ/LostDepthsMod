@echo off
echo Starting GUI Designer on http://localhost:8090
echo Press Ctrl+C to stop
cd /d "%~dp0"
powershell -Command "Start-Process 'http://localhost:8090'"
powershell -ExecutionPolicy Bypass -File "%~dp0server.ps1"
