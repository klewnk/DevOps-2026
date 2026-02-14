pipeline {
    agent any
    environment {
        DOCKER_USER = 'klewnk'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/postgres-db"
    }
    stages {
        stage('Docker Build & Push') {
            steps {
                sh '''
                docker build -t $DOCKER_PREFIX:latest -f postgres.Dockerfile .
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Component Testing') {
            steps {
                sh '''
                echo "--- Starting Local Component Test ---"
                docker rm -f test-db || true
                
                # Χρησιμοποιούμε την 5433 για να μην συγκρουστούμε με τη "ζωντανή" βάση
                docker run -d --name test-db -p 5433:5432 -e POSTGRES_PASSWORD=pass12345 $DOCKER_PREFIX:latest
                
                sleep 15
                nc -z localhost 5433 && echo "Database is REACHABLE" || (docker rm -f test-db && exit 1)
                
                echo "--- Test passed, cleaning up test container ---"
                docker rm -f test-db
                '''
            }
        }

        stage('Production Deploy') { // <--- ΑΥΤΟ ΛΕΙΠΕΙ ΓΙΑ ΤΗΝ ΕΡΓΑΣΙΑ
            steps {
                sh '''
                echo "--- Deploying to Production ---"
                # Εδώ χρησιμοποιούμε το docker-compose για να το σηκώσουμε μόνιμα
                docker compose up -d db
                '''
            }
        }
    }
    
    post {
        always {
            // Διασφαλίζουμε ότι το test container θα σβηστεί ακόμα και αν το test αποτύχει
            sh 'docker rm -f test-db || true'
        }
    }
}