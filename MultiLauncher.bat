@echo off
title Building Java MultiLauncher

echo ==========================================
echo        Building Java MultiLauncher
echo ==========================================

REM --- Define root paths ---
set ROOT=%cd%

set GAME_SRC=%ROOT%\src\Java
set TODO_SRC=%ROOT%\src\TodoApp

set GAME_CLASSES=%ROOT%\classes\Java
set TODO_CLASSES=%ROOT%\classes\Todo

REM --- Delete old launcher ---
set "oldgame=..\MultiLauncher.exe"
if exist "%oldgame%" (
    echo Deleting old build...
    del "%oldgame%"
)

REM --- Clean class folders ---
if exist "%GAME_CLASSES%" rmdir /s /q "%GAME_CLASSES%"
if exist "%TODO_CLASSES%" rmdir /s /q "%TODO_CLASSES%"

mkdir "%GAME_CLASSES%"
mkdir "%TODO_CLASSES%"

REM =========================================================
REM Step 1: Compile Java sources
REM =========================================================
echo [1/6] Compiling Java source files...

javac -d "%GAME_CLASSES%" .\src\Java\*.java
if errorlevel 1 (
    echo Java Game compilation failed!
    pause
    exit /b
)

javac -d "%TODO_CLASSES%" .\src\TodoApp\*.java
if errorlevel 1 (
    echo TodoApp compilation failed!
    pause
    exit /b
)

REM =========================================================
REM Step 2: Build Game.jar
REM =========================================================
echo [2/6] Creating Game.jar...
jar cfm finishedjar\Game.jar src\Java\MANIFEST.MF -C "%GAME_CLASSES%" . -C images .
if errorlevel 1 (
    echo Failed to create Game.jar!
    pause
    exit /b
)

REM =========================================================
REM Step 3: Build Todo.jar
REM =========================================================
echo [3/6] Creating Todo.jar...
jar cfm finishedjar\Todo.jar src\TodoApp\MANIFEST.MF -C "%TODO_CLASSES%" .
if errorlevel 1 (
    echo Failed to create Todo.jar!
    pause
    exit /b
)

REM =========================================================
REM Step 4: Copy JARs into launcher folder
REM =========================================================
echo [4/6] Copying JARs into Launcher...
copy /Y finishedjar\Game.jar Launcher\Game.jar >nul
copy /Y finishedjar\Todo.jar Launcher\Todo.jar >nul

REM =========================================================
REM Step 5: Compile launcher executable
REM =========================================================
echo [5/6] Compiling launcher executable...
cd Launcher

windres resources.rc -o resources.o
g++ MultiLauncher.cpp resources.o -o "..\MultiLauncher" -mwindows ^
    -ldwmapi -lmsimg32 -lgdi32 -lole32 -lcomctl32 -lstdc++fs

if errorlevel 1 (
    echo Launcher build failed!
    pause
    exit /b
)

cd "%ROOT%"

REM =========================================================
REM Done
REM =========================================================
echo [6/6] Build complete!
echo ------------------------------------------
echo MultiLauncher.exe is ready to run
echo ------------------------------------------
pause
