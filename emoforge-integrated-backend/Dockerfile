FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY emoforge-app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Xms256m","-Xmx512m","-XX:+UseG1GC","-XX:+UseContainerSupport","-jar","app.jar"]
