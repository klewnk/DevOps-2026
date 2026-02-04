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
              sh 'ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/docker_deploy.yaml'
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