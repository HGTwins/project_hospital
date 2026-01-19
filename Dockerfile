# 1. 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
# 실행 권한 부여 및 빌드 (테스트는 제외하여 속도 향상)
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar
# 포트 설정
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]