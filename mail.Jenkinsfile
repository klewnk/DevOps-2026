pipeline {
    agent any

    environment {
        DOCKER_TOKEN=credentials('docker-push-secret')
        DOCKER_USER='klewnk'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/klewnk/mailhog'
    }

    stages {
        stage('Docker build and push') {
            steps {
                sh '''
                HEAD_COMMIT=$(git rev-parse --short HEAD)
                TAG=$HEAD_COMMIT-$BUILD_ID
                docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f mail.Dockerfile .
            '''

                sh '''
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                docker push $DOCKER_PREFIX --all-tags
            '''
            }
        }

        stage('Pull and Run Mailhog') {
            steps {
                sh '''
                docker pull $DOCKER_PREFIX:latest
                echo "Running Mailhog container..."
                docker run -d --name test-mailhog -p 8025:8025 $DOCKER_PREFIX:latest
            '''
            }
        }

        stage('Test Mailhog') {
            steps {
                sh '''
                sleep 3
                echo "Testing Mailhog Web UI..."
                curl --fail http://localhost:8025 || exit 1
                nc -z localhost 8025 || (echo "Mailhog Web UI not responding" && exit 1)
                '''
            }
        }

        stage('Cleanup') {
            steps {
                sh '''
                    echo "Cleaning up the mailhog container now..."
                    docker stop test-mailhog || true
                    docker rm -f test-mailhog || true
                    ! lsof -i :8025 && echo "✅ Port 8025 is free" || (echo "❌ Port 8025 still in use" && exit 1)

            '''
            }
        }
    }

    post {
        always {
            mail(
                to: 'it2022041@hua.gr',
                from: 'it2022041@hua.gr',
                body: "Project ${env.JOB_NAME} <br> Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}",
                mimeType: 'text/html'
            )
        }
    }
}