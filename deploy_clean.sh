#!/bin/bash

cd ~/Geosphere_Java_BE || { echo "❌ Failed to cd into /GeoSphere_Java_BE"; exit 1; }

echo "=== Starting SSH Agent and Adding Key ==="
eval "$(ssh-agent -s)"
ssh-add /root/.ssh/geosphere.pem

echo "=== Pulling Latest Code ==="
git fetch origin
git checkout main
git pull origin main --force

echo "=== Verifying Latest Commit ==="
git log -1 --oneline

echo "=== Cleaning and Installing Java Application ==="
mvn clean install

echo "=== Killing Any Process on Port 8080 ==="
PORT_PID=$(lsof -ti:8080)
if [ -n "$PORT_PID" ]; then
  echo "⚠️ Killing process on port 8080 (PID: $PORT_PID)"
  kill -9 $PORT_PID
fi

# Kill existing screen session
if screen -list | grep -q "geosphere-java-be"; then
  echo "⚠️ Killing old screen session: geosphere-java-be"
  screen -S geosphere-java-be -X quit
fi

echo "=== Starting Java Application in Screen Session ==="
screen -dmS geosphere-java-be bash -c 'mvn spring-boot:run'

echo "=== Waiting for Application to Start ==="
sleep 5

echo "=== Verifying Java Application Process ==="
JAVA_PID=$(pgrep -f "GeoSphere_Java_BE")
if [ -z "$JAVA_PID" ]; then
  echo "❌ Java application failed to start. Check logs with: screen -r geosphere-java-be"
  exit 1
else
  echo "✅ Java application is running with PID(s): $JAVA_PID"
fi

echo "=== Checking if screen session 'geosphere-java-be' is running ==="
if screen -list | grep -q "geosphere-java-be"; then
  echo "✅ Screen session 'geosphere-java-be' is running."
else
  echo "❌ Screen session 'geosphere-java-be' NOT running. Check screen logs manually."
  exit 1
fi

echo "=== Running Tests (if any) ==="
# If you're running tests in this project (e.g., with JUnit)
if command -v mvn &> /dev/null; then
  mvn test || echo "⚠️ Tests failed or not found"
else
  echo "⚠️ Maven not found. Skipping tests."
fi

echo "✅ Deployment Complete"
