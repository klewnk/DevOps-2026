pipeline {
    agent any

    environment {
        // Ορίζουμε το Image Name για ευκολία
        DOCKER_IMAGE = "ghcr.io/klewnk/mailhog"
        // Χρησιμοποιούμε το Jenkins Build Number ή το Git Commit για το Tag
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}-${env.BUILD_NUMBER}"
        // Credentials ID που έχεις ορίσει στο Jenkins για το GHCR
        DOCKER_CREDS = credentials('DOCKER_TOKEN') 
    }

    stages {
        stage('Docker build and push') {
            steps {
                script {
                    // Build την εικόνα με το commit hash και το latest tag
                    sh "docker build --rm -t ${DOCKER_IMAGE}:${IMAGE_TAG} -t ${DOCKER_IMAGE}:latest -f mail.Dockerfile ."
                    
                    // Login και Push στο GitHub Container Registry
                    sh "echo ${DOCKER_TOKEN} | docker login ghcr.io -u klewnk --password-stdin"
                    sh "docker push ${DOCKER_IMAGE} --all-tags"
                }
            }
        }

        stage('Pull and Run Mailhog') {
            steps {
                script {
                    sh "docker pull ${DOCKER_IMAGE}:latest"
                    
                    // ΔΙΟΡΘΩΣΗ: Διαγραφή παλιού container αν υπάρχει για να αποφευχθεί το Conflict
                    sh "docker rm -f test-mailhog || true"
                    
                    echo "Running Mailhog container..."
                    sh "docker run -d --name test-mailhog -p 8025:8025 -p 1025:1025 ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Test Mailhog') {
            steps {
                script {
                    echo "Testing if Mailhog is up..."
                    // Ένα απλό check αν η θύρα 8025 ανταποκρίνεται
                    sh "curl -f http://localhost:8025"
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Cleaning up..."
                // Σταματάμε και διαγράφουμε τον container μετά το τέλος του pipeline
                sh "docker stop test-mailhog || true"
                sh "docker rm test-mailhog || true"
            }
        }
        success {
            echo "Pipeline finished successfully!"
        }
        failure {
            echo "Pipeline failed. Check the logs."
            // Εδώ μπορείς να προσθέσεις το mail notification που είδα στο log σου
            mail to: 'admin@example.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Something went wrong with build ${env.BUILD_NUMBER}"
        }
    }
}