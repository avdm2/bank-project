FROM openjdk:17-jdk-slim

# docker build --platform linux/arm64 .

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]