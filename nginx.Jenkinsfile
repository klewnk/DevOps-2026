pipeline {
    agent any
    
    environment {
        DOCKER_USER = 'klewnk'
        // Το όνομα που θα έχει το image σου στο GitHub
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/nginx-custom"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/klewnk/DevOps-2026.git'
            }
        }

        stage('Docker Login') {
            steps {
                // Χρησιμοποιούμε το Token σου για να έχουμε δικαίωμα Push
                withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                    sh "echo \$DOCKER_TOKEN | docker login ghcr.io -u ${DOCKER_USER} --password-stdin"
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh '''
                echo "--- Building Custom Nginx Image ---"
                # Χτίζουμε το image χρησιμοποιώντας το Dockerfile σου
                docker build -t $DOCKER_PREFIX:latest -f nginx.Dockerfile .
                
                echo "--- Pushing to GitHub Registry ---"
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Integration Test') {
            steps {
                sh '''
                echo "--- Τρέχουμε το ΝΕΟ Image τοπικά για δοκιμή ---"
                docker rm -f nginx-test || true
                docker run -d --name nginx-test -p 8085:80 $DOCKER_PREFIX:latest
                
                sleep 5
                
                # Έλεγχος αν απαντάει
                curl --fail http://localhost:8085 && echo "✅ Image is Working!" || (docker rm -f nginx-test && exit 1)
                
                echo "--- Cleanup Test Container ---"
                docker rm -f nginx-test
                '''
            }
        }

        stage('Final Deploy (Optional)') {
            steps {
                // Εδώ το Ansible ή το Docker Compose αναλαμβάνει να τραβήξει το νέο image στον Cloud Server
                echo "Το Image είναι έτοιμο στο: $DOCKER_PREFIX:latest"
            }
        }
    }

    post {
        failure {
            echo "❌ Κάτι πήγε λάθος στο Build ή στο Push."
        }
    }
}