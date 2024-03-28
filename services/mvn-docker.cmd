@echo off
mvn clean package docker:build -D org.slf4j.simpleLogger.showDateTime=true -D org.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss,SSS
