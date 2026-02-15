pipeline {
    agent any

    environment {
        DOCKER_TOKEN = credentials('github-token')
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        // Χρησιμοποιούμε το σωστό όνομα για το Mailhog
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
                # Εδώ κανονικά θα έκανες build το Dockerfile σου
                # Αλλά αν θες να τεστάρεις με ένα έτοιμο image:
                docker pull mailhog/mailhog:latest 
                docker tag mailhog/mailhog:latest $DOCKER_PREFIX:$TAG
                docker tag mailhog/mailhog:latest $DOCKER_PREFIX:latest
                
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

        stage('Run Component') {
            steps {
                sh """
                    docker pull $DOCKER_PREFIX:latest
                    echo "Running Mailhog container..."
                    docker run -d --name ${CONTAINER_NAME} -p 8025:8025 $DOCKER_PREFIX:latest
                """
            }
        }

        stage('Test Component') {
            steps {
                echo "Waiting for service to be healthy..."
                // To Mailhog ακούει στην 8025 για το UI
                sh 'sleep 5 && curl --fail http://localhost:8025 || exit 1'
            }
        }
    }

    post {
        always {
            // Αυτό είναι το "μαγικό" σημείο: Καθαρίζει ΠΑΝΤΑ, είτε πέτυχε το test είτε όχι
            echo 'Final cleanup of test containers...'
            sh "docker rm -f ${CONTAINER_NAME} || true"
            
            mail(
                to: 'it2022041@hua.gr',
                body: "Mailhog Build status: ${currentBuild.currentResult}\nProject: ${env.JOB_NAME}\nBuild: ${env.BUILD_NUMBER}",
                subject: "JENKINS: Mailhog Component Build",
            )
        }
    }
}