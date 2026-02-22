🚀 DevOps 2026 

📍 1. Προετοιμασία & Git Clone

Αρχικά, κατεβάζουμε το repository στο VM:
Bash

git clone https://github.com/klewnk/devops-project.git
cd devops-project

🔑 2. SSH Keys & Επικοινωνία

Για την ασφαλή σύνδεση μεταξύ του Laptop, του Jenkins VM και του Deployment VM:

    Δημιουργία κλειδιών: ssh-keygen -t rsa.

    Ανταλλαγή κλειδιών: Προσθήκη του id_rsa.pub στο ~/.ssh/authorized_keys των VMs.

    Config: Ρύθμιση του ~/.ssh/config για εύκολη σύνδεση με ονόματα αντί για IP.

🛠️ 3. Εγκατάσταση Απαραίτητων Εργαλείων (Jenkins VM)

Στο Jenkins VM εγκαθιστούμε τα εξής:
Java JDK 21

Απαραίτητο για το Spring Boot 3+ και το Jenkins:
Bash

sudo apt update
sudo apt install openjdk-21-jdk

Jenkins

Ακολουθούμε τον επίσημο οδηγό:
Bash

# Ενδεικτικά για Docker run:
docker run -d -p 8080:8080 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

# Για Native εγκατάσταση:
sudo systemctl enable jenkins
sudo systemctl start jenkins

    Initial Password: sudo cat /var/lib/jenkins/secrets/initialAdminPassword

☁️ 4. Ρυθμίσεις Δικτύου (GCloud Firewall)

Πρέπει να ανοίξουν οι παρακάτω θύρες στο Google Cloud Console:

    80 / 443: HTTP/HTTPS (Nginx)

    8080: Jenkins / Spring Boot

    8081: Adminer

    8025: Mailhog

    22: SSH

    5432: PostgreSQL

🚀 5. Deployment & Χρήση (Jenkins Jobs)

Αφού τρέξουν τα Jenkins Pipelines, η πρόσβαση γίνεται μέσω της κεντρικής πύλης:
🔗 URLs Υπηρεσιών

    Κύρια Εφαρμογή: https://klewn-devops.duckdns.org/

    Διαχείριση Βάσης (Adminer): https://klewn-devops.duckdns.org/db/

    Έλεγχος Emails (Mailhog): https://klewn-devops.duckdns.org/mail/      







    
