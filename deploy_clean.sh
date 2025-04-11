#!/bin/bash

# Navigate to project directory
cd /Geosphere_Java_BE || exit 1

echo "🔁 Pulling latest changes from Git..."
git pull origin main || { echo "❌ Git pull failed"; exit 1; }

echo "🧹 Cleaning and building the project..."
./mvnw clean package -DskipTests -Dspring.profiles.active=dev || { echo "❌ Maven build failed"; exit 1; }

echo "🛑 Killing existing screen session 'springboot' if any..."
screen -S springboot -X quit 2>/dev/null

echo "🚀 Starting new screen session 'springboot' with dev profile..."
screen -dmS springboot bash -c 'java -jar target/*.jar --spring.profiles.active=dev'

echo "✅ Deployed with clean install. App running in screen session 'springboot'."
