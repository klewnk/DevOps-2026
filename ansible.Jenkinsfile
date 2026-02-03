pipeline {
    agent any

    environment {
        // Ορίζουμε ότι η Ansible θα χρησιμοποιεί το τοπικό config
        ANSIBLE_CONFIG = "${WORKSPACE}/ansible.cfg"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Κατεβάζει τον κώδικα από το Git repository [cite: 14, 17]
                checkout scm
            }
        }

        stage('Inventory Connectivity Check') {
            steps {
                script {
                    echo "Checking connectivity to Google Cloud VM..."
                    // Χρησιμοποιεί το γκρουπ 'googlevms' από το hosts.yaml σου
                    sh "ansible googlevms -m ping"
                }
            }
        }

        stage('Install Docker on GCloud') {
            steps {
                script {
                    echo "Starting Docker installation via Ansible..."
                    // Εκτελεί το κύριο playbook. 
                    // Το ansible.cfg θα βρει αυτόματα το hosts.yaml [cite: 43]

                    
                      sh "ansible-playbook ansible-devops/playbooks/docker_deploy.yaml"
                    
                }
            }
        }

        stage('Verify Docker Installation') {
            steps {
                script {
                    echo "Verifying Docker versions on remote host..."
                    // Επιβεβαίωση ότι το docker και το compose είναι έτοιμα [cite: 18, 21, 26]
                    sh "ansible googlevms -a 'docker --version'"
                    sh "ansible googlevms -a 'docker compose version'"
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline finished."
        }
    }
}