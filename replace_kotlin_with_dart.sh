#!/usr/bin/env bash
# replace_kotlin_with_dart.sh
# Run this script to replace the Kotlin/Android project with the Flutter/Dart project

set -euo pipefail

ROOT="/workspace/bba99bf9-dfd5-47ce-b4ec-bd91955a4d41/sessions/agent_9164272d-2da0-4dc0-8e8b-ba9560024736"
FLUTTER_SOURCE="$ROOT/flutter_nexus_dev"

echo "=== Nexus Dev Orchestrator: Replace Kotlin with Dart ==="
echo "Root: $ROOT"
echo ""

# 1. Remove Kotlin/Android project files
echo "1. Removing Kotlin/Android project..."
rm -rf "$ROOT/app"
rm -f "$ROOT/build.gradle.kts"
rm -f "$ROOT/settings.gradle.kts"
rm -f "$ROOT/gradle.properties"
rm -f "$ROOT/gradle/libs.versions.toml"
rm -f "$ROOT/proguard-rules.pro"
rm -rf "$ROOT/gradle"

# 2. Remove old .gitignore and .env.example (will use Flutter's)
rm -f "$ROOT/.gitignore"
rm -f "$ROOT/.env.example"

# 3. Move Flutter project to root
echo "2. Moving Flutter project to root..."
cp -r "$FLUTTER_SOURCE/"* "$ROOT/"
cp -r "$FLUTTER_SOURCE/." "$ROOT/" 2>/dev/null || true

# 4. Copy hidden files
echo "3. Copying hidden files..."
cp "$FLUTTER_SOURCE/.gitignore" "$ROOT/.gitignore" 2>/dev/null || echo "No .gitignore in Flutter source"

# 5. Remove the flutter_nexus_dev subdirectory
echo "4. Cleaning up..."
rm -rf "$FLUTTER_SOURCE"

# 6. Verify structure
echo "5. Verifying new structure..."
echo ""
echo "Root directory contents:"
ls -la "$ROOT"
echo ""
echo "lib directory:"
ls -la "$ROOT/lib" 2>/dev/null | head -20
echo ""
echo "android directory:"
ls -la "$ROOT/android" 2>/dev/null | head -10
echo ""
echo "ios directory:"
ls -la "$ROOT/ios" 2>/dev/null | head -10

echo ""
echo "=== Done! ==="
echo "The repository now contains the Flutter/Dart project at root level."
echo "Next steps:"
echo "  cd $ROOT"
echo "  flutter pub get"
echo "  flutter pub run build_runner build --delete-conflicting-outputs"
echo "  flutter run"