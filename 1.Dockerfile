FROM openjdk:26-ea-25-slim-trixie
WORKDIR /app
COPY target/project20205-0.0.1-SNAPSHOT.jar ./application.jar

CMD ["java", "-jar" , "application.jar"]