FROM openjdk:11.0.7-jre-slim
ENV PORT=9000
EXPOSE ${PORT}
ARG JAR_FILE=target/file-bench-server.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]