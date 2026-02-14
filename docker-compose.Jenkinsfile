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
                echo "--- Καθαρισμός προηγούμενων αποτυχημένων δοκιμών ---"
                # Σβήνουμε τα πάντα από το jenkins-test αν υπάρχουν
                docker compose -p jenkins-test down --volumes --remove-orphans || true
                
                # ΑΝ έχεις σταθερά ονόματα στο compose, τα σβήνουμε και χειροκίνητα για σιγουριά
                docker rm -f postgres_container mailhog_container spring_app_container nginx_container || true

                echo "--- Σήκωμα Stack τοπικά για δοκιμή ---"
                docker compose -p jenkins-test up -d
                
                echo "--- Αναμονή 30 δευτερολέπτων ---"
                sleep 30
                
                echo "--- Έλεγχος επικοινωνίας Nginx -> Java ---"
                # Δοκιμάζουμε στην IP του docker0 (172.17.0.1) επειδή το localhost στην πόρτα 80 μπορεί να είναι πιασμένο
                HOST_IP=$(ip route show | grep docker0 | awk '{print $9}')
                curl --fail http://${HOST_IP}:80 || (echo "❌ Test Failed!" && docker compose -p jenkins-test down && exit 1)
                
                echo "✅ Όλα τα containers επικοινωνούν σωστά!"
                
                echo "--- Cleanup Test Environment ---"
                docker compose -p jenkins-test down --volumes
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