pipeline {
    agent any

    environment {
        // Ορίζουμε το path για το config του Ansible για να μη χάνεται
        ANSIBLE_CONFIG = "${WORKSPACE}/ansible-devops/ansible.cfg"
        ANSIBLE_HOST_KEY_CHECKING = 'False'
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Κατεβάζει τα YAML σου από το GitHub
                checkout scm
            }
        }

        stage('Infrastructure Check') {
            steps {
                script {
                    echo 'Επιβεβαίωση σύνδεσης με τον Server...'
                    // Χρησιμοποιούμε το δικό σου host: devops_gcloud1
                    sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -m ping"
                }
            }
        }

        stage('Full Deployment (DB, Email, App)') {
            steps {
                script {
                    echo 'Έναρξη καθολικής εγκατάστασης μέσω Ansible...'
                    // Τρέχουμε το master playbook που εισάγει τα 3 αρχεία σου
                    sh "ansible-playbook -i ansible-devops/hosts.yaml ansible-devops/playbooks/main_deploy.yaml"
                }
            }
        }

        stage('Verification') {
            steps {
                echo 'Έλεγχος αν οι υπηρεσίες είναι up...'
                // Τσεκάρουμε αν ακούνε οι πόρτες 5432 (DB), 8025 (Mailhog) και 8080 (App)
                sh "ansible -i ansible-devops/hosts.yaml devops_gcloud1 -a 'netstat -tulpn'"
            }
        }
    }
    
    post {
        success {
            echo 'Η εφαρμογή και οι βάσεις εγκαταστάθηκαν επιτυχώς!'
        }
        failure {
            echo 'Κάτι πήγε στραβά. Έλεγξε τα logs του συγκεκριμένου Playbook.'
        }
    }
}