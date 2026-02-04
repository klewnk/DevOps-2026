pipeline {
    agent any

    environment {
        // Ορισμός στοιχείων για το GitHub Container Registry
        DOCKER_USER = 'klewnk'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/postgres-db"
    }

    stages {
        stage('Docker Build & Push') {
            steps {
                sh '''
                # Χτίσιμο του image από το τοπικό Dockerfile
                docker build -t $DOCKER_PREFIX:latest -f postgres.Dockerfile .
                
                # Ανέβασμα στο GHCR
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Component Testing') {
            steps {
                sh '''
                echo "--- Starting Local Component Test ---"
                
                # Καθαρίζουμε τυχόν παλιά containers για να μην έχουμε port conflict (5432)
                docker rm -f test-db || true
                
                # Προσωρινό σήκωμα της βάσης για έλεγχο
                docker run -d --name test-db -p 5432:5432 -e POSTGRES_PASSWORD=pass12345 $DOCKER_PREFIX:latest
                
                # Αναμονή για να προλάβει να κάνει initialize η Postgres
                sleep 10
                
                # Έλεγχος αν η πόρτα 5432 ανταποκρίνεται
                nc -z localhost 5432 && echo "Database is REACHABLE" || (echo "Database Connection FAILED" && exit 1)
                
                echo "--- Test passed, cleaning up test container ---"
                
                # Απελευθέρωση της πόρτας μετά το test
                docker stop test-db
                docker rm test-db
                '''
            }
        }
    }

    post {
        success {
            echo 'Το component χτίστηκε και ελέγχθηκε επιτυχώς!'
        }
        failure {
            echo 'Κάτι πήγε λάθος στο build ή στο port check.'
        }
    }
}