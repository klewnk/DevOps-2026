pipeline {
    agent any

    environment {
        // GitHub Container Registry
        DOCKER_TOKEN = credentials('github-token') // Το ID από Jenkins Credentials
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/mailhog"
    }

    stages {
        stage('Docker Build and Push') {
            steps {
                sh '''
                # Δημιουργία Tag με βάση το Commit και το Build ID
                HEAD_COMMIT=$(git rev-parse --short HEAD)
                TAG=$HEAD_COMMIT-$BUILD_ID
                
                # Build χρησιμοποιώντας Dockerfile
                docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f mail.Dockerfile .
                
                # Login και Push στο GHCR
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

        stage('Pull and Run Test') {
            steps {
                sh '''
                docker pull $DOCKER_PREFIX:latest
                echo "Starting temporary container for testing..."
                docker run -d --name test-mailhog -p 8025:8025 $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Test Component') {
            steps {
                sh '''
                sleep 5
                echo "Checking if Mailhog UI responds..."
                curl --fail http://localhost:8025 || exit 1
                '''
            }
        }

     stage('Production Deploy') {
    steps {
        // Η εντολή αυτή διασφαλίζει ότι το Jenkins τρέχει στο σωστό σημείο
        sh '''
        docker compose -f docker-compose.yaml pull mailhog
        docker compose -f docker-compose.yaml up -d mailhog
        '''
    }
}
}

        stage('Cleanup') {
            steps {
                sh '''
                echo "Cleaning up test container..."
                docker stop test-mailhog || true
                docker rm -f test-mailhog || true
                '''
            }
        }
    }

    post {
        always {
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