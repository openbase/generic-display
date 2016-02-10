#!/bin/bash
APP_NAME='generic-display'
clear &&
echo "=== clean ${APP_NAME} ===" &&
mvn clean $@ &&
clear &&
echo "=== install and deploy ${APP_NAME} ===" &&
mvn install -DassembleDirectory=${prefix} $@ &&
clear &&
echo "=== ${APP_NAME} is successfully installed to ${prefix} ==="
