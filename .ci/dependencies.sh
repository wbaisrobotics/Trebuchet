#!/usr/bin/env bash
set -e

target="$(dirname "$0")/../lib/"
libraries=(WPILib NetworkTables)
path='AIS-Robotics/Dependencies/master/wpilib'
base="https://raw.githubusercontent.com/$path"

mkdir -p "$target"
cd "$target"
for lib in "${libraries[@]}"
do
	echo "Downloading dependency: ${lib}..."
	echo "$base/$lib.jar"
	curl -o "${lib}.jar" "$base/${lib}.jar"
	[ -f "${lib}.jar" ]
done
cd ..
mvn install:install-file \
	-Dfile=lib/WPILib.jar \
	-DgroupId=edu.wpi.first.wpilibj \
	-DartifactId=wpilibJava \
	-Dversion=0.1.0-SNAPSHOT \
	-Dpackaging=jar
mvn install:install-file \
	-Dfile=lib/NetworkTables.jar \
	-DgroupId=edu.wpi.first.wpilib.networktables.java \
	-DartifactId=NetworkTables \
	-Dversion=0.1.0-SNAPSHOT \
	-Dpackaging=jar
