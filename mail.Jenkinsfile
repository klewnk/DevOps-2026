pipeline {
    agent any

    environment {
        DOCKER_TOKEN = credentials('github-token')
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/mailhog"
    }

    stages {
        stage('Initial Cleanup') {
            steps {
                echo 'Cleaning up any leftover containers from failed builds...'
                // Αυτό θα τρέχει ΠΑΝΤΑ στην αρχή για να ελευθερώνει το όνομα
                sh 'docker rm -f test-mailhog || true'
            }
        }

        stage('Docker Build and Push') {
            steps {
                sh '''
                HEAD_COMMIT=$(git rev-parse --short HEAD)
                TAG=$HEAD_COMMIT-$BUILD_ID
                
                # Build το image από το Dockerfile σου
                docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f mail.Dockerfile .
                
                # Login και Push
                echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

        stage('Run Mailhog') {
            steps {
                sh '''
                docker pull $DOCKER_PREFIX:latest
                docker run -d --name test-mailhog -p 8025:8025 $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Test Mailhog') {
            steps {
                // Δίνουμε 5 δευτερόλεπτα στο container να σηκωθεί
                sh 'sleep 5 && curl --fail http://localhost:8025'
            }
        }
    }

    post {
        always {
            // Εδώ είναι το κλειδί: Καθαρίζει το container είτε πέτυχε το build είτε όχι
            echo 'Final cleanup...'
            sh 'docker rm -f test-mailhog || true'
            
            mail(
                to: 'it2022041@hua.gr',
                subject: "JENKINS: Mailhog Build ${currentBuild.currentResult}",
                body: "Build Number: ${env.BUILD_NUMBER}\nStatus: ${currentBuild.currentResult}"
            )
        }
    }
}