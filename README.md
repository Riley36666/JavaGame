# 🎮 JavaGame

Welcome to **JavaGame**, a small 2D game built entirely in **Java Swing**.  
This was my **first ever programming project**, created to learn the fundamentals of game development, graphics, and user input handling in Java.  
It was made with occasional help from **AI assistance** when I needed guidance. 🤖

---

## 🕹️ Overview

JavaGame is a simple desktop game that includes:
- A **Start Menu**
- A **Level Selector**
- An **Options Menu**
- Multiple playable **Levels**
- Custom **graphics and settings**

It’s a lightweight, pure Java project — no external libraries or game engines.

---

## ✨ Features

✅ Classic 2D graphics using `JPanel` and `Graphics2D`  
✅ Level and settings system (`settings.properties`)  
✅ Keyboard input and basic animations  
✅ Menu screens for navigation  
✅ Organized assets (images, sounds)

---

## 🧠 What I Learned

This project helped me understand:
- How to structure a Java project
- How rendering loops and frame updates work
- How to handle keyboard input in Swing
- How to organize files and manage game state
- The importance of clean code and modularity

---

## 🚀 How to Run


### 🧩 Running from the Command Line

```bash
# 1️⃣ Clone the repository
git clone https://github.com/Riley36666/JavaGame.git
cd JavaGame

# 2️⃣ Compile all Java source files into a 'classes' folder
cd Java
javac -d classes *.java
jar cfm finishedjar/Game.jar MANIFEST.MF -C classes . -C images .

java -jar finishedjar/Game.jar
