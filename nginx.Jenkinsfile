pipeline {
    agent any

    stages {
        stage('Prepare & Run Test') {
            steps {
                sh '''
                echo "--- Καθαρισμός παλιών containers ---"
                docker rm -f nginx-standalone-test || true
                
                echo "--- Εκκίνηση Nginx για δοκιμή ---"
                # Χρησιμοποιούμε την 8085 για να μην πέσουμε πάνω σε Java/Adminer
                docker run -d --name nginx-standalone-test -p 8085:80 nginx:alpine
                
                echo "--- Αναμονή 5 δευτερολέπτων ---"
                sleep 5
                '''
            }
        }
                         

        stage('Health Check') {
            steps {
                sh '''
                HOST_IP=$(ip route show | grep docker0 | awk '{print $9}')
                echo "Testing on Host IP: $HOST_IP"
                
                curl --fail http://${HOST_IP}:8085 || (echo "❌ Failed to connect" && exit 1)
                echo "✅ Ο Nginx λειτουργεί κανονικά."

                '''
            }
        }

        stage('Cleanup') {
            steps {
                sh '''
                echo "--- Σβήσιμο container και απελευθέρωση πόρτας 8085 ---"
                docker stop nginx-standalone-test
                docker rm nginx-standalone-test
                
                # Επιβεβαίωση ότι η πόρτα έκλεισε
                if lsof -i :8085; then
                    echo "❌ Η πόρτα 8085 είναι ακόμα πιασμένη!"
                    exit 1
                else
                    echo "✅ Η πόρτα 8085 είναι ελεύθερη."
                fi
                '''
            }
        }
    }
}