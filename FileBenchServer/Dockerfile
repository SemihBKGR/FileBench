FROM openjdk:11.0.7-jre-slim
ENV PORT=8080
EXPOSE ${PORT}
ARG JAR_FILE=target/gateway-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]