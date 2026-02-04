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
                // Εδώ καλούμε το ansible-playbook
                // Προσοχή: Το path πρέπει να είναι αυτό που έχεις στο VM σου
                sh 'ansible-playbook -i inventory.ini ansible-devops/playbooks/docker_deploy.yaml'
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