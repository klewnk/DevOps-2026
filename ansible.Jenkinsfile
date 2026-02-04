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

        stage('Infra Setup (Docker)') {
            steps {
                script {
                    echo 'Εγκατάσταση Docker και προετοιμασία περιβάλλοντος...'
                    // Τρέχει το playbook που περιέχει το include_tasks: docker.yaml
                    sh 'ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/docker_deploy.yaml --tags "install"'
                }
            }
        }

        stage('Deploy Application (Containers)') {
            steps {
                script {
                    echo 'Εκκίνηση εφαρμογής με Docker Compose...'
                    // Τρέχει ΜΟΝΟ το κομμάτι του deployment
                    sh 'ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/docker_deploy.yaml --tags "deploy"'
                }
            }
        }
    }
}