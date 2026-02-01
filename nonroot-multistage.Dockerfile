FROM maven:3.9.11-amazoncorretto-24-alpine AS build


WORKDIR /build

COPY pom.xml .
COPY src/ ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine-3.21

MAINTAINER klewn
WORKDIR /app

RUN apk update && apk add curl

RUN addgroup -S appgroup && adduser -S appuser -G appgroup 

COPY --from=build /build/target/project20205-0.0.1-SNAPSHOT.jar ./application.jar

RUN chown appuser /app/application.jar

USER appuser 

EXPOSE 8080
ENTRYPOINT [ "java", "-jar" , "application.jar" ]