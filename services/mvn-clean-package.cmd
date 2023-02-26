@echo off
mvn clean package -D org.slf4j.simpleLogger.showDateTime=true -D org.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss,SSS
