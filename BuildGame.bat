@echo off
title Building Java Game

echo ==========================================
echo        Building JavaGame
echo ==========================================

REM --- Define root paths ---
set ROOT=%cd%
set SRC=%ROOT%\src\Java
set CLASSES=%ROOT%\classes\Java

REM --- Delete old build if it exists ---
set "oldgame=..\My Game (WIP name).exe"
if exist "%oldgame%" (
    echo Deleting old build...
    del "%oldgame%"
)

REM --- Clean and recreate classes folder at root ---
if exist "%CLASSES%" (
    echo Removing old classes directory...
    rmdir /s /q "%CLASSES%"
)
mkdir "%CLASSES%"

REM --- Step 1: compile Java files ---
echo [1/5] Compiling Java source files...
javac -d "%CLASSES%" .\src\Java\*.java
if errorlevel 1 (
    echo Java compilation failed!
    pause
    exit /b
)

REM --- Step 2: build the JAR file ---
echo [2/5] Creating Game.jar...
jar cfm finishedjar\Game.jar src\Java\MANIFEST.MF -C "%CLASSES%" . -C images .
if errorlevel 1 (
    echo Failed to create Game.jar!
    pause
    exit /b
)

REM --- Step 3: copy jar into launcher folder ---
echo [3/5] Copying JAR into Launcher...
copy /Y finishedjar\Game.jar Launcher\Game.jar >nul

REM --- Step 4: compile resources and launcher ---
echo [4/5] Compiling launcher executable...
cd Launcher
windres resources.rc -o resources.o
g++ launch.cpp resources.o -o "..\My Game (WIP name)" -mwindows
if errorlevel 1 (
    echo Launcher build failed!
    pause
    exit /b
)
cd "%ROOT%"

echo [5/5] Build complete!
echo ------------------------------------------
echo My Game (WIP name).exe is ready to run
echo ------------------------------------------
pause
