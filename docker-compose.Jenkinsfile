pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/klewnk/DevOps-2026.git'
            }
        }

        stage('Local Integration Test') {
            steps {
                sh '''
                echo "--- Καθαρισμός και έλεγχος θυρών ---"
                # Σβήνουμε τα πάντα
                docker compose -p jenkins-test down --volumes --remove-orphans || true
                docker rm -f adminer_container postgres_container mailhog_container spring_app_container nginx_container || true
                
                # ΕΛΕΓΧΟΣ: Αν η πόρτα 80 είναι πιασμένη από 'native' Nginx, τον σταματάμε προσωρινά
                sudo systemctl stop nginx || true 

                echo "--- Σήκωμα Stack τοπικά για δοκιμή ---"
                docker compose -p jenkins-test up -d
                
                echo "--- Αναμονή 30 δευτερολέπτων ---"
                sleep 30
                
                echo "--- Έλεγχος επικοινωνίας ---"
                # Δοκιμάζουμε στο localhost (αφού πλέον η 80 θα είναι του Docker)
                curl --fail http://localhost:80 || (echo "❌ Test Failed!" && docker compose -p jenkins-test down && exit 1)
                
                echo "✅ Όλα OK!"
                
                echo "--- Cleanup ---"
                docker compose -p jenkins-test down --volumes
                
                # Ξαναξεκινάμε τον κανονικό Nginx αν χρειάζεται
                sudo systemctl start nginx || true
                '''
            }
        }
        
        stage('Build & Deploy with Ansible') {
            steps {
                sshagent(['gcloud-ssh-key']) {
                    withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                        echo "--- Ξεκινάει το Deploy στον Cloud Server μέσω Ansible ---"
                        sh "DOCKER_TOKEN=${DOCKER_TOKEN} ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/docker_deploy.yaml"
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Το Integration Test πέρασε ΚΑΙ το Deployment τελείωσε επιτυχώς!'
        }
        failure {
            echo ' Κάτι πήγε λάθος. Αν κόπηκε στο Test, ο Cloud Server έμεινε ανέπαφος (ασφαλής).'
        }
    }
}