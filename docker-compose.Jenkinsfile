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
        sshagent(['gcloud-ssh-key']) {
            // Προσθέτουμε το DOCKER_TOKEN στο environment της εντολής
            withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                sh "DOCKER_TOKEN=${DOCKER_TOKEN} ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/docker_deploy.yaml"
            }
        }
    }
}
    }
    
    post {
        success {
            echo 'Deployment Finished Successfully!'
        }
        failure {
            echo 'Deployment Failed. Check Ansible logs.'
        }
    }
}