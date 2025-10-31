# 1️⃣ Compile everything into 'classes' folder
javac -d classes *.java

# 3️⃣ Build the JAR (with manifest)
jar cfm finishedjar/Game.jar MANIFEST.MF -C classes .
