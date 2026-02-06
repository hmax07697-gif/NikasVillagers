#!/bin/bash
# Build script for NamedVillagers plugin

echo "========================================="
echo "  NamedVillagers Build Script"
echo "========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/"
    exit 1
fi

echo "Cleaning previous builds..."
mvn clean

echo ""
echo "Building plugin..."
mvn package

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "  Build successful!"
    echo "========================================="
    echo ""
    echo "Plugin JAR location:"
    echo "  -> target/NamedVillagers-1.0.0.jar"
    echo ""
    echo "Copy this file to your server's plugins/ folder"
else
    echo ""
    echo "========================================="
    echo "  Build failed!"
    echo "========================================="
    exit 1
fi
