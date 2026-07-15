#!/bin/bash
set -e

echo "1. Gradle 빌드 중..."
./gradlew clean build

echo "2. 배포 패키지 구성 중..."
rm -rf deploy-package deploy.zip
mkdir -p deploy-package

JAR_FILE=$(find build/libs -name "*.jar" ! -name "*-plain.jar" | head -n 1)
cp "$JAR_FILE" deploy-package/application.jar
cp -r .platform deploy-package/

cd deploy-package
zip -r ../deploy.zip . -x ".*"
cd ..

echo "3. EB 배포 중..."
eb deploy

echo "완료!"
