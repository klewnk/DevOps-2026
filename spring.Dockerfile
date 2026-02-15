FROM maven:3.9.11-amazoncorretto-24-alpine AS build


WORKDIR /build

COPY pom.xml .
COPY src/ ./src

RUN mvn clean package -DskipTests


FROM openjdk:26-ea-25-slim-trixie
WORKDIR /app
COPY --from=build build/target/project20205-0.0.1-SNAPSHOT.jar ./application.jar

CMD ["java", "-jar" , "application.jar"]