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
                echo "--- Σκληρός καθαρισμός παλιών containers ---"
                # 1. Σβήνει το test stack αν υπάρχει
                docker compose -p jenkins-test down --volumes --remove-orphans || true
                
                # 2. Σβήνει ΟΠΟΙΟΔΗΠΟΤΕ container τρέχει με τα συγκεκριμένα ονόματα (adminer, spring, postgres, κτλ)
                # Χρησιμοποιούμε μια λίστα για να είμαστε σίγουροι
                docker rm -f adminer_container postgres_container mailhog_container spring_app_container nginx_container || true
                
                echo "--- Σήκωμα Stack τοπικά για δοκιμή ---"
                docker compose -p jenkins-test up -d
                
                echo "--- Αναμονή 30 δευτερολέπτων ---"
                sleep 30
                
                echo "--- Έλεγχος επικοινωνίας ---"
                # Χρησιμοποιούμε την IP του docker host για το curl
                HOST_IP=$(ip route show | grep docker0 | awk '{print $9}')
                curl --fail http://${HOST_IP}:80 || (echo "❌ Test Failed!" && docker compose -p jenkins-test down && exit 1)
                
                echo "✅ Όλα OK!"
                
                echo "--- Cleanup ---"
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