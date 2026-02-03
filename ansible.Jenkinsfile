pipeline {
    agent any

    environment {
        // ΔΙΟΡΘΩΣΗ 1: Το config είναι μέσα στον φάκελο ansible-devops
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
                    // ΔΙΟΡΘΩΣΗ 2: Προσθέτουμε το "ansible-devops/" μπροστά από το hosts.yaml
                    sh 'ansible -i ansible-devops/hosts.yaml devops_gcloud1 -m ping'
                }
            }
        }

        stage('Install Docker on GCloud') {
            steps {
                script {
                    echo 'Deploying Docker...'
                    // ΔΙΟΡΘΩΣΗ 3: Και εδώ το path στο inventory
                    // Το path του playbook (ansible-devops/playbooks/...) ήταν ήδη σωστό!
                    sh 'ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/docker_deploy.yaml'
                }
            }
        }

        stage('Verify Installation') {
            steps {
                script {
                    echo 'Verifying Docker version...'
                    // ΔΙΟΡΘΩΣΗ 4: Και εδώ τα paths
                    sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -a 'docker --version'"
                    sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -a 'docker compose version'"
                }
            }
        }
    }
}