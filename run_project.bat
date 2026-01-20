@echo off
echo Starting FixPix...

REM Start Backend in a new Command Prompt window
start cmd /k "cd backend && python manage.py runserver"

REM Start Frontend in a new Command Prompt window
start cmd /k "cd website && npm run dev"
