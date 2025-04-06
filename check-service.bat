@echo off
set SERVICE_NAME=lms-service-auth
SET PORT=9091
FOR /F "tokens=5" %%A IN ('netstat -ano ^| findstr :%PORT%') DO (
    echo Service %SERVICE_NAME% is running on port %PORT%
    exit /b 0
)
echo Service %SERVICE_NAME% is stopped on port %PORT%
exit /b 1
