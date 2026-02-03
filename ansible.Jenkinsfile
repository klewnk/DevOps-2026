pipeline {
    agent any

    environment {
        ANSIBLE_CONFIG = "${WORKSPACE}/ansible-devops/ansible.cfg"
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
                    sh 'ansible -i ansible-devops/hosts.yaml devops_gcloud1 -m ping'
                }
            }
        }

        stage('Install Docker on GCloud') {
            steps {
                script {
                    echo 'Deploying Docker...'

                    sh 'ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/docker_deploy.yaml'
                }
            }
        }

        stage('Verify Installation') {
            steps {
                script {
                    echo 'Verifying Docker version...'
                    
                    sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -a 'docker --version'"
                    sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -a 'docker compose version'"
                }
            }
        }
    }
}