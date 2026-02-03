pipeline {
    agent any

    environment {
        // Αυτό βοηθάει να μην κολλάει στο "Are you sure you want to connect?"
        ANSIBLE_HOST_KEY_CHECKING = 'False'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Test connection to deploy env') {
            steps {
                script {
                    echo 'Testing connectivity...'
                    // Ελέγχουμε μόνο το Google Cloud προς το παρόν που είμαστε σίγουροι ότι δουλεύει
                    sh 'ansible -i hosts.yaml devops_gcloud1 -m ping'
                }
            }
        }

        stage('Install Docker on GCloud') {
            steps {
                script {
                    echo 'Deploying Docker...'
                    // Βεβαιώσου ότι αυτό το path είναι σωστό στο Git σου!
                    sh 'ansible-playbook -i hosts.yaml ansible-devops/playbooks/docker_deploy.yaml'
                }
            }
        }

        stage('Verify Installation') {
            steps {
                script {
                    echo 'Verifying Docker version...'
                    sh "ansible -i hosts.yaml devops_gcloud1 -a 'docker --version'"
                    sh "ansible -i hosts.yaml devops_gcloud1 -a 'docker compose version'"
                }
            }
        }
    }
}