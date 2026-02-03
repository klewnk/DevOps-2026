pipeline {
    agent any

    environment {
        // Βάλε εδώ το σωστό path για το inventory σου
        INVENTORY_PATH = "${WORKSPACE}/ansible-devops/host.yaml" 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Deploy Native DB') {
            steps {
                sshagent(['id_devops_key']) {
                    sh """
                        # Τρέχουμε το Native Playbook
                        ansible-playbook -i ${INVENTORY_PATH} ansible-devops/playbooks/postgres.yaml \
                        --private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
                        --user kleonkola
                    """
                }
            }
        }
    }
}