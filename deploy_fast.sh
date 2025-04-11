#!/bin/bash

# Navigate to project directory
cd /Geosphere_Java_BE || exit 1

echo "🔁 Pulling latest changes from Git..."
git pull origin main || { echo "❌ Git pull failed"; exit 1; }

echo "🛑 Killing existing screen session 'springboot' if any..."
screen -S springboot -X quit 2>/dev/null

echo "🚀 Starting new screen session 'springboot' using mvn spring-boot:run..."
screen -dmS springboot bash -c './mvnw spring-boot:run'

echo "✅ Quick deploy done. App running in screen session 'springboot'."
