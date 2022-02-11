FROM maven:3.8.4-openjdk-11-slim AS build
COPY src /filebench/src
COPY pom.xml /filebench
RUN mvn -f /filebench/pom.xml -q clean package
FROM openjdk:11-slim
COPY --from=build /filebench/target/file-bench-server-1.0.0.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","app.jar"]