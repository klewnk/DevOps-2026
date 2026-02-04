pipeline {
    agent any

    environment {
        INVENTORY_PATH = "${WORKSPACE}/ansible-devops/inventory.ini"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Deploy Docker Scenario') {
            steps {
                // Χρειαζόμαστε το SSH key ΚΑΙ τον κωδικό του Docker (token)
                withCredentials([
                    sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER'),
                    string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')
                ]) {
                    sh """
                        chmod 600 \${SSH_KEY}

                        # Τρέχουμε το Docker Playbook
                        # Περνάμε το Token ως μεταβλητή (extra-vars) στο Ansible
                        ansible-playbook -i ${INVENTORY_PATH} ansible-devops/playbooks/deploy_docker.yaml \
                        --private-key=\${SSH_KEY} \
                        --ssh-common-args='-o StrictHostKeyChecking=no' \
                        --user kleonkola \
                        -e "docker_token=\${DOCKER_TOKEN}"
                    """
                }
            }
        }
    }
}