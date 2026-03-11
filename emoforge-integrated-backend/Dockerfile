FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY emoforge-app/build/libs/*.jar app.jar

# 로그 디렉토리 생성
RUN mkdir -p /app/logs

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -XX:+UseG1GC -XX:+UseContainerSupport -jar app.jar"]
