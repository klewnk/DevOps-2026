# Χρησιμοποιούμε Java 17 (ή την έκδοση που έχεις)
FROM eclipse-temurin:17-jdk-alpine

# Δημιουργούμε έναν φάκελο για την εφαρμογή
WORKDIR /app

# Αντιγράφουμε όλο τον κώδικα μέσα στον container
COPY . .

# Χτίζουμε το JAR (παρακάμπτοντας τα tests για ταχύτητα)
RUN ./mvnw clean package -DskipTests

# Εκτελούμε το JAR που παρήχθη
ENTRYPOINT ["java", "-jar", "target/*.jar"]