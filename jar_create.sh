#!/bin/bash

set -e

echo "=== Pulling Latest Code ==="
git fetch origin
git checkout main
git pull origin main --force

echo "=== Cleaning and Installing Java Application ==="
./mvnw clean install -DskipTests

echo "=== Creating/Clearing JArs Directory in Project ==="
mkdir -p ./JArs
rm -f ./JArs/*.jar

echo "=== Copying Built JAR to ./JArs ==="
JAR_PATH=$(find target -type f -name "*.jar" | head -n 1)
if [ -z "$JAR_PATH" ]; then
  echo "❌ No JAR file found in target directory!"
  exit 1
fi

JAR_NAME=$(basename "$JAR_PATH")
cp "$JAR_PATH" "./JArs/$JAR_NAME"

echo "=== Adding and Committing JAR to Git ==="
git add "./JArs/$JAR_NAME" -f
git commit -m "Add built JAR to ./JArs"
git push origin main

echo "✅ Build and Deployment Completed Successfully"
