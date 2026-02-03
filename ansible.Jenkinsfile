pipeline {
    agent any

    environment {
        // Ορίζουμε το config path για να μην το γράφουμε συνέχεια
        ANSIBLE_CONFIG = "${WORKSPACE}/ansible.cfg"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Κατεβάζει τον κώδικα από το Git
                checkout scm
            }
        }

        stage('Inventory Connectivity Check') {
            steps {
                script {
                    echo "Testing connection to Google Cloud VM..."
                    // Δοκιμάζει αν το devops_gcloud1 απαντάει (χρησιμοποιώντας το host_vars)
                    sh "ansible googlevms -m ping -i hosts.yaml"
                }
            }
        }

        stage('Install Docker') {
            steps {
                script {
                    echo "Running Docker Installation Playbook..."
                    // Τρέχει το playbook σου
                    sh "ansible-playbook -i hosts.yaml ansible-devops/playbooks/docker_deploy.yaml"
                }
            }
        }

        stage('Verify Installation') {
            steps {
                script {
                    echo "Verifying Docker version on Remote VM..."
                    // Επιβεβαιώνει ότι το Docker εγκαταστάθηκε όντως
                    sh "ansible googlevms -a 'docker --version' -i hosts.yaml"
                    sh "ansible googlevms -a 'docker compose version' -i hosts.yaml"
                }
            }
        }
    }

    post {
        success {
            echo "Deployment and Verification Successful!"
        }
        failure {
            echo "Pipeline failed. Check the logs above."
        }
    }
}