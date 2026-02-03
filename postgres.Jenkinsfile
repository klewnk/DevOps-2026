pipeline {
    agent any

    environment {
        // Το path για το inventory file
        INVENTORY_PATH = "${WORKSPACE}/ansible-devops/inventory.ini"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Deploy Native DB') {
            steps {
                // Χρησιμοποιούμε withCredentials αντί για sshagent που δεν υπάρχει
                withCredentials([sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER')]) {
                    sh """
                        # Ρυθμίζουμε τα δικαιώματα του κλειδιού (για ασφάλεια)
                        chmod 600 \${SSH_KEY}

                        # Τρέχουμε το Native Playbook
                        # Περνάμε το κλειδί ρητά με το --private-key
                        ansible-playbook -i ${INVENTORY_PATH} ansible-devops/playbooks/postgres.yaml \
                        --private-key=\${SSH_KEY} \
                        --ssh-common-args='-o StrictHostKeyChecking=no' \
                        --user kleonkola
                    """
                }
            }
        }
    }
}