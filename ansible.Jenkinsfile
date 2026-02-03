pipeline {
    agent any

    environment {
        // Λέμε στην Ansible πού είναι το config file
        ANSIBLE_CONFIG = "${WORKSPACE}/ansible.cfg"
        // Απενεργοποιούμε το host checking για να μην κολλάει στο "yes/no"
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
                    // -i hosts.yaml: Διαβάζει το αρχείο που φτιάξαμε στο Βήμα 2
                    // devops_gcloud1: Το όνομα που ξέρει το ~/.ssh/config σου
                    sh 'ansible -i ansible-devops/hosts.yaml devops_azure1 -m ping'
                }
            }
        }

        stage('Install Docker on GCloud') {
            steps {
                script {
                    echo 'Deploying Docker...'
                    // Τρέχουμε το playbook
                    // Προσοχή: Το path είναι ansible-devops/playbooks/docker_deploy.yaml
                    sh 'ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/docker_deploy.yaml'
                }
            }
        }

        stage('Verify Installation') {
            steps {
                script {
                    echo 'Verifying Docker version...'
                    sh "ansible -i ansible-devops/hosts.yaml devops_azure1 -a 'docker --version'"
                    sh "ansible -i ansible-devops/hosts.yaml devops_azure1 -a 'docker compose version'"
                }
            }
        }
    }
}