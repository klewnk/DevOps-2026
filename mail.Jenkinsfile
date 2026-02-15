pipeline {
    agent any

    environment {
        // GitHub Container Registry
        DOCKER_TOKEN = credentials('github-token') // Το ID από Jenkins Credentials
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/mailhog"
        CONTAINER_NAME = "test-mailhog" // Μία μεταβλητή για όλα τα stages
    }

    stages {
        stage('Cleanup Environment') {
            steps {
                echo 'Cleaning up before we start...'
                // Σβήνουμε το σωστό container name
                sh "docker rm -f ${CONTAINER_NAME} || true"
            }
        }

        stage('Docker pull and push') {
            steps {
                sh '''
                HEAD_COMMIT=$(git rev-parse --short HEAD)
                TAG=$HEAD_COMMIT-$BUILD_ID
                docker pull nginx:alpine
                docker tag nginx:alpine $DOCKER_PREFIX:$TAG
                docker tag nginx:alpine $DOCKER_PREFIX:latest

            '''

                sh '''
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                docker push $DOCKER_PREFIX --all-tags
            '''
            }
        }

        stage('Pull and run nginx') {
            steps {
                sh '''
                    docker pull $DOCKER_PREFIX:latest
                    echo "Running nginx container..."
                    docker run -d --name test-nginx -p 8081:80 $DOCKER_PREFIX:latest
                '''
            }
        }

    stage('Test Component') {
            steps {
                sh 'sleep 5 && curl --fail http://localhost:8025 || exit 1'
            }
        }

        stage('Cleanup Test') { // Μεταφέρουμε το cleanup ΠΡΙΝ το deploy
            steps {
                sh 'docker rm -f test-mailhog || true'
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