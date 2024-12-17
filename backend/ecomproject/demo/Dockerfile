FROM maven:3.8.6-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml mvnw* ./

COPY mvn/wrapper/maven-wrapper.properties ./mvn/wrapper/

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]