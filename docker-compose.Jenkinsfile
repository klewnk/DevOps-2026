pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/klewnk/DevOps-2026.git'
            }
        }

        stage('Build & Deploy with Ansible') {
            steps {
                // Χρήση του SSH Key για σύνδεση στον Cloud Server
                sshagent(['gcloud-ssh-key']) {
                    // Χρήση του Docker Token για το login στο Registry 
                    withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                        sh """
                        DOCKER_TOKEN=${DOCKER_TOKEN} ansible-playbook \
                        -i ansible-devops/inventory.ini \
                        ansible-devops/playbooks/docker_deploy.yaml
                        """
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Deployment Finished Successfully!'
        }
        failure {
            echo '❌ Deployment Failed. Check logs in the console output.'
        }
    }
}