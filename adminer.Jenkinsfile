pipeline {
    agent any
    environment {
        DOCKER_USER = 'klewnk'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/adminer"
    }

    stages {




      stage('Cleanup Old Containers') {
         steps {
           sh 'docker rm -f adminer_container || true' 
         }
       }


        stage('Docker Build & Push') {
            steps {
                sh '''
                docker build -t $DOCKER_PREFIX:latest -f adminer.Dockerfile .
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }
        stage('Component Testing') {
            steps {
                sh '''
                echo "--- Starting Adminer Test ---"
                docker rm -f test-adminer || true
                
                # Τρέχουμε το test στην 8081 για να μην έχουμε conflict
                docker run -d --name test-adminer -p 8081:8080 $DOCKER_PREFIX:latest
                
                sleep 5
                # Έλεγχος αν η σελίδα επιστρέφει κώδικα (HTTP 200)
                curl --fail http://localhost:8081 && echo "✅ Adminer UI is UP" || (docker rm -f test-adminer && exit 1)
                
                docker rm -f test-adminer
                '''
            }
        }
        stage('Production Deploy') {
    steps {
        sh 'docker compose up -d --no-deps adminer' 
    }
}
    }
    post {
        always {
            sh 'docker rm -f test-adminer || true'
        }
    }
}