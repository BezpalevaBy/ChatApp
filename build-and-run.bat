@echo off
setlocal enabledelayedexpansion

echo 🔧 Building Angular frontend...
cd frontend
call npm install
call npx ng build --configuration production
if errorlevel 1 (
    echo ❌ Angular build failed.
    pause
    exit /b 1
)

echo 📦 Copying Angular build to backend static folder...
cd ..
set STATIC_PATH=backend\src\main\resources\static

:: Удаляем старую статику
rmdir /s /q %STATIC_PATH%
mkdir %STATIC_PATH%

:: Копируем всё содержимое из dist/browser/ в static/
xcopy /s /y /i "frontend\dist\frontend\browser\*.*" "%STATIC_PATH%\"
xcopy /s /y /i "frontend\dist\frontend\browser\assets\*" "%STATIC_PATH%\assets\"

echo ✅ Static files copied from dist\browser\ directly.

echo ✅ Copy completed.

echo 🛠️ Building Spring Boot backend...
cd backend
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ❌ Spring Boot build failed.
    pause
    exit /b 1
)

echo 🚀 Starting backend using start_backend.bat in new console...
cd ..
start "Spring Boot Backend" cmd /k start_back.bat

echo ✅ Done. Spring Boot is running in a separate window.
pause
