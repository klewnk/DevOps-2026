pipeline {
    agent any

    environment {
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/mailhog"
        DOCKER_TOKEN = credentials('github-token')
    }

    stages {
        // 1. CLEANUP ΣΤΗΝ ΑΡΧΗ - Οπως ακριβώς στον Adminer
        stage('Cleanup Old Containers') {
            steps {
                echo "Cleaning up any old test containers..."
                sh 'docker rm -f test-mailhog || true'
            }
        }

        // 2. BUILD & PUSH - Χρησιμοποιούμε το δικό σου mail.Dockerfile
        stage('Docker Build & Push') {
            steps {
                sh '''
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                
                # Build με latest tag
                docker build -t $DOCKER_PREFIX:latest -f mail.Dockerfile .
                
                # Push στο GitHub Container Registry
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        // 3. COMPONENT TESTING - Με έλεγχο στο port 8025 του Mailhog
        stage('Component Testing') {
            steps {
                sh '''
                echo "--- Starting Mailhog Test ---"
                docker rm -f test-mailhog || true
                
                # Τρέχουμε τον container για test
                docker run -d --name test-mailhog -p 8025:8025 -p 1025:1025 $DOCKER_PREFIX:latest
                
                sleep 10
                
                # Έλεγχος αν απαντάει το UI
                curl --fail http://localhost:8025 && echo "✅ Mailhog UI is UP" || (docker rm -f test-mailhog && exit 1)
                
                # Καθαρίζουμε τον test container αφού πέτυχε
                docker rm -f test-mailhog
                '''
            }
        }

        // 4. PRODUCTION DEPLOY
        stage('Production Deploy') {
            steps {
                echo "Deploying to production via Docker Compose..."
                // Εδώ σιγουρέψου ότι στο docker-compose.yml το service λέγεται mailhog
                sh 'docker compose up -d --no-deps mailhog'
            }
        }
    }

    post {
        always {
            // Cleanup για να μη μείνουν σκουπίδια αν αποτύχει κάτι
            sh 'docker rm -f test-mailhog || true'
            
            mail(
                to: 'it2022041@hua.gr',
                from: 'it2022041@hua.gr',
                body: "Mailhog Build status: ${currentBuild.currentResult}\nBuild Number: ${env.BUILD_NUMBER}",
                subject: "JENKINS: Mailhog Component Build #${env.BUILD_NUMBER}",
                mimeType: 'text/html'
            )
        }
    }
}