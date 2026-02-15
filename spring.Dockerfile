# --- Stage 1: Build ---
# Χρησιμοποιούμε μια εικόνα που έχει ήδη το Maven εγκατεστημένο
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Αντιγράφουμε τα αρχεία του project
COPY . .

# Χτίζουμε το JAR χρησιμοποιώντας το 'mvn' του συστήματος (όχι το ./mvnw)
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
# Χρησιμοποιούμε μια ελαφριά έκδοση Java για να τρέξει η εφαρμογή
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Παίρνουμε ΜΟΝΟ το JAR αρχείο από το προηγούμενο στάδιο (build)
# Αυτό μειώνει το μέγεθος του τελικού image δραματικά
COPY --from=build /app/target/*.jar app.jar

# Τρέχουμε την εφαρμογή
ENTRYPOINT ["java", "-jar", "app.jar"]