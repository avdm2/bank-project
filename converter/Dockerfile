FROM docker
COPY --from=docker/buildx-bin:latest /buildx /usr/libexec/docker/cli-plugins/docker-buildx
RUN docker buildx version

FROM openjdk:17-jdk-slim

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]