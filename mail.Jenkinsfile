pipeline {
    agent any
    environment {
        DOCKER_USER = 'klewnk'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/mailhog"
        DOCKER_TOKEN = credentials('github-token')
    }

    stages {
        // 1. Καθάρισμα στην αρχή όπως είπαμε
        stage('Cleanup Old Containers') {
            steps {
                sh 'docker rm -f test-mailhog || true'
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh '''
                echo $DOCKER_TOKEN | docker login ghcr.io -u $DOCKER_USER --password-stdin
                docker build -t $DOCKER_PREFIX:latest -f mail.Dockerfile .
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Component Testing') {
            steps {
                sh '''
                echo "--- Starting Mailhog Test ---"
                docker rm -f test-mailhog || true
                
                # Τρέχουμε το test (8025 για το UI)
                docker run -d --name test-mailhog -p 8025:8025 -p 1025:1025 $DOCKER_PREFIX:latest
                
                sleep 5
                # Έλεγχος αν το Mailhog UI είναι UP
                curl --fail http://localhost:8025 && echo "✅ Mailhog UI is UP" || (docker rm -f test-mailhog && exit 1)
                
                # Προαιρετικό: Το σβήνουμε μετά το τεστ αν πέτυχε
                docker rm -f test-mailhog
                '''
            }
        }

        stage('Production Deploy') {
            steps {
                // Προσαρμογή ανάλογα με το docker-compose σου
                sh 'docker compose up -d --no-deps mailhog' 
            }
        }
    }

    post {
        always {
            // Διασφάλιση ότι δεν θα μείνει τίποτα όρθιο αν αποτύχει το test
            sh 'docker rm -f test-mailhog || true'
            
            mail(
                to: 'it2022041@hua.gr',
                from: 'it2022041@hua.gr',
                body: "Mailhog Build status: ${currentBuild.currentResult}",
                subject: "JENKINS: Mailhog Component Build",
                mimeType: 'text/html'
            )
        }
    }
}