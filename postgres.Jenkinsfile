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

        stage('Test Postgres Component') {
            steps {
                sh '''
                # Σηκώνουμε το container τοπικά στον Jenkins για test
                docker run -d --name test-db -p 5432:5432 -e POSTGRES_PASSWORD=pass12345 $DOCKER_PREFIX:latest
                
                # Περιμένουμε λίγο να ξεκινήσει
                sleep 5
                
                # Έλεγχος αν η πόρτα είναι ανοιχτή
                nc -z localhost 5432 && echo "✅ Postgres is Up" || (echo "❌ Fail" && exit 1)
                
                # Cleanup του test
                docker stop test-db
                docker rm test-db
                '''
            }
        }
    }
}