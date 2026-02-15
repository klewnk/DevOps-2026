pipeline {
    agent any

        environment {
        // GitHub Container Registry
        DOCKER_TOKEN = credentials('github-token') // Το ID από Jenkins Credentials
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/spring-app"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                    sh '''
                    # Login στο GitHub Registry
                    echo $DOCKER_TOKEN | docker login ghcr.io -u $DOCKER_USER --password-stdin
                    
                    # Build το νέο image της εφαρμογής
                    docker build -t $DOCKER_PREFIX:latest -f spring.Dockerfile .
                    
                    # Push στο GitHub
                    docker push $DOCKER_PREFIX:latest
                    '''
                }
            }
        }

        stage('Kubernetes Deploy (via Ansible)') {
            steps {
                // Χρήση του SSH key για να μιλήσει ο Jenkins στον server που έχει το MicroK8s
                sshagent(['gcloud-ssh-key']) {
                    sh '''
                    ansible-playbook -i ansible-devops/inventory.ini \
                    kubernetes/k8s_deploy.yaml \
                    --user kleonkola
                    '''
                }
            }
        }

        stage('Verification') {
            steps {
                sh "microk8s kubectl get pods"
                sh "microk8s kubectl get ingress"
                echo "✅ Deployment complete! Check your FQDN."
            }
        }
    }

    post {
        success {
            echo '🚀 Η εφαρμογή είναι online στο Kubernetes!'
        }
    }
}