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
                echo "--- Καθαρισμός προηγούμενων δοκιμών ---"
                docker compose -p jenkins-test down --volumes --remove-orphans || true
                docker rm -f adminer_container postgres_container mailhog_container spring_app_container nginx_container || true
                
                echo "--- Σήκωμα Stack στην πόρτα 8089 (on-the-fly) ---"
                # Εδώ περνάμε την 8089 μόνο για το test
                NGINX_PORT=8089 docker compose -p jenkins-test up -d
                
                echo "--- Αναμονή 30 δευτερολέπτων ---"
                sleep 30
                
                echo "--- Έλεγχος στην πόρτα 8089 ---"
                curl --fail http://localhost:8089 || (echo "❌ Test Failed!" && NGINX_PORT=8089 docker compose -p jenkins-test down && exit 1)
                
                echo "✅ Το Test πέρασε στην 8089!"
                
                echo "--- Cleanup ---"
                NGINX_PORT=8089 docker compose -p jenkins-test down --volumes
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