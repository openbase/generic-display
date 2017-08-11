#!/bin/bash
APP_NAME='generic-display'
clear &&
echo "=== clean ${APP_NAME} ===" &&
mvn clean $@ &&
clear &&
echo "=== install and deploy ${APP_NAME} ===" &&
mvn install \
        -DassembleDirectory=${prefix} \
        -DskipTests=true \
        -Dmaven.test.skip=true \
        -Dlicense.skipAddThirdParty=true \
        -Dlicense.skipUpdateProjectLicense=true \
        -Dlicense.skipDownloadLicenses \
        -Dlicense.skipCheckLicense=true \
        -Dmaven.license.skip=true $@ &&
clear &&
echo "=== ${APP_NAME} is successfully installed to ${prefix} ==="
