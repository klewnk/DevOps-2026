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
            # 1. Προετοιμασία φακέλου και Login στο Azure
            ansible all -i ansible-devops/host.yaml -m shell \
            -a "mkdir -p ~/app" \
            --private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
            -e "ansible_host=${AZURE_IP} ansible_user=azureuser" --limit azurevm-1

            # 2. Αντιγραφή του αρχείου (Χρησιμοποίησε SCP όπως το έχεις, είναι ΟΚ)
            scp -o StrictHostKeyChecking=no -i ${SSH_KEY} docker-compose.yaml azureuser@${AZURE_IP}:~/app/docker-compose.yaml

            # 3. ΚΑΘΑΡΙΣΜΟΣ ΚΑΙ ΕΚΚΙΝΗΣΗ (Εδώ είναι το κλειδί για την Port 8080)
            ansible all -i ansible-devops/host.yaml -m shell \
            -a "cd ~/app && echo '${DOCKER_TOKEN}' | docker login ghcr.io -u ${DOCKER_USER} --password-stdin && docker compose down && docker compose pull && docker compose up -d" \
            --private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
            -e "ansible_host=${AZURE_IP} ansible_user=azureuser" --limit azurevm-1

ansible all -i ansible-devops/host.yaml -m shell \
-a "sudo systemctl stop postgresql || true && cd ~/app && echo '${DOCKER_TOKEN}' | docker login ghcr.io -u ${DOCKER_USER} --password-stdin && docker compose down && docker compose up -d" \
--private-key=${SSH_KEY} --ssh-common-args='-o StrictHostKeyChecking=no' \
-e "ansible_host=${AZURE_IP} ansible_user=azureuser" --limit azurevm-1
            """
           }
            }
        }
    }
}