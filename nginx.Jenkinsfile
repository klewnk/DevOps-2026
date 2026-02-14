pipeline {
    agent any
    environment {
        DOCKER_PREFIX = "ghcr.io/klewnk/nginx"
    }
    stages {
        stage('Docker Build & Push') {
            steps {
                sh '''
                docker build -t $DOCKER_PREFIX:latest -f nginx.Dockerfile .
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }
        stage('Component Testing') {
            steps {
                sh '''
                docker rm -f test-nginx || true
                # Τρέχουμε το test στην 8082 για να μην έχουμε conflict
                docker run -d --name test-nginx -p 8082:80 $DOCKER_PREFIX:latest
                sleep 5
                curl --fail http://localhost:8082 && echo "✅ Nginx is UP"
                docker rm -f test-nginx
                '''
            }
        }
        stage('Production Deploy') {
            steps {
                sh 'docker compose up -d --no-deps web'
            }
        }
    }
    post {
        always {
            // Εδώ κρατάμε το email notification αν το θέλεις
            echo "Build finished with status: ${currentBuild.currentResult}"
        }
    }
}