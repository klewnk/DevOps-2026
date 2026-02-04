pipeline {
    agent any
    environment {
        DOCKER_USER = 'klewnk'
        DOCKER_PREFIX = "ghcr.io/${DOCKER_USER}/postgres-db"
        INVENTORY_PATH = "${WORKSPACE}/ansible-devops/inventory.ini"
    }
    stages {
        stage('Docker Build & Push') {
            steps {
                sh '''
                docker build -t $DOCKER_PREFIX:latest -f postgres.Dockerfile .
                # Εδώ υποθέτουμε ότι έχεις κάνει login στο GHCR ήδη
                docker push $DOCKER_PREFIX:latest
                '''
            }
        }

        stage('Deploy via Ansible') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY')]) {
                    sh """
                        chmod 600 ${SSH_KEY}
                        ansible-playbook -i ${INVENTORY_PATH} ansible-devops/playbooks/postgres.yaml \
                        --private-key=${SSH_KEY} \
                        --ssh-common-args='-o StrictHostKeyChecking=no' \
                        --user kleonkola \
                        --extra-vars "image_name=$DOCKER_PREFIX:latest"
                    """
                }
            }
        }
        
        stage('Verify Database Port') {
            steps {
                // Ελέγχουμε αν η πόρτα 5432 είναι ανοιχτή στο VM
                sh 'nc -z -v 34.51.255.13 5432 || echo "Database is starting..."'
            }
        }
    }
}