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
                echo "--- Σήκωμα Stack τοπικά για δοκιμή ---"
                # Χρησιμοποιούμε προσωρινό όνομα project για να μην έχουμε conflicts
                docker compose -p jenkins-test up -d
                
                echo "--- Αναμονή 30 δευτερολέπτων για να 'ξυπνήσει' η Java ---"
                sleep 30
                
                echo "--- Έλεγχος επικοινωνίας Nginx -> Java ---"
                # Ελέγχουμε αν ο Nginx στην πόρτα 80 μας απαντάει
                curl --fail http://localhost:80 || (echo "❌ Test Failed!" && docker compose -p jenkins-test down && exit 1)
                
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