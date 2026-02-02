pipeline {
    agent any

    environment {
        // Στοιχεία για το GitHub Container Registry
        DOCKER_REGISTRY = 'ghcr.io'
        DOCKER_USER = 'klewnk' // Το username σου στο GitHub
        IMAGE_NAME = "ghcr.io/${DOCKER_USER}/spring-app"
        
        // IPs των VMs
        AZURE_IP = '20.208.128.155'
        GCLOUD_IP = '34.51.245.90'
    }

    stages {
        stage('Docker Build & Push') {
            steps {


                // Εντολή για να κατέβουν τα αρχεία μέσα από το submodule
                sh 'git submodule update --init --recursive'

                withCredentials([string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')]) {
                    sh """
                    # Χτίσιμο του Spring App χρησιμοποιώντας το Dockerfile σου
                    docker build -t ${IMAGE_NAME}:latest -f nonroot-multistage.Dockerfile .
                    
                    # Login και Push στο GitHub
                    echo ${DOCKER_TOKEN} | docker login ${DOCKER_REGISTRY} -u ${DOCKER_USER} --password-stdin
                    docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy to VMs (via Ansible)') {
            steps {
                withCredentials([
                    sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY'),
                    string(credentialsId: 'docker-push-secret', variable: 'DOCKER_TOKEN')
                ]) {
                    sh """
                    # 1. Deployment στο Azure
                    ansible all -i ansible-devops/host.yaml -m shell \
                    -a "mkdir -p ~/app && cd ~/app && echo '${DOCKER_TOKEN}' | docker login ghcr.io -u ${DOCKER_USER} --password-stdin" \
                    --private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
                    -e "ansible_host=${AZURE_IP} ansible_user=azureuser" --limit azurevm-1

                    # 2. Αντιγραφή του docker-compose.yml στο VM
                    scp -o StrictHostKeyChecking=no -i ${SSH_KEY} docker-compose.yml azureuser@${AZURE_IP}:~/app/docker-compose.yml

                    # 3. Docker Compose Up
                    ansible all -i ansible-devops/host.yaml -m shell \
                    -a "cd ~/app && docker-compose pull && docker-compose up -d" \
                    --private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
                    -e "ansible_host=${AZURE_IP} ansible_user=azureuser" --limit azurevm-1
                    """
                    
                    // Εδώ μπορείς να επαναλάβεις τα ίδια βήματα για το Google VM αλλάζοντας την IP και το User
                }
            }
        }
    }
}