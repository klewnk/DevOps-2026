pipeline {
    agent any

    environment {
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'klewnk'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = 'ghcr.io/klewnk/DevOps-2026'
        AZURE_IP = '20.208.128.155'
        GCLOUD_IP = '34.51.245.90'
    }

    stages {
        stage('Network Check (NC)') {
            steps {
                echo "--- Checking if Ports 22 are open ---"
                sh "nc -zv -w 5 ${AZURE_IP} 22"
                sh "nc -zv -w 5 ${GCLOUD_IP} 22"
            }
        }

       stage('Ansible Ping (SSH Login)') {
    steps {
        echo "--- Testing Login with Correct Usernames ---"
        withCredentials([sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY')]) {
            sh """
            # Για το Azure
            ansible all -i ansible-devops/host.yaml -m ping \
            --private-key=${SSH_KEY} \
            --ssh-common-args='-o StrictHostKeyChecking=no' \
            -e "ansible_host=20.208.128.155 ansible_user=azureuser" --limit azurevm-1

            # Για το Google Cloud
            ansible all -i ansible-devops/host.yaml -m ping \
            --private-key=${SSH_KEY} \
            --ssh-common-args='-o StrictHostKeyChecking=no' \
            -e "ansible_host=34.51.245.90 ansible_user=kleonkola" --limit googlevm-1
            """
        }
    }
}
    }

    post {
        success {
            echo "✅ SUCCESS: Το δίκτυο είναι ανοιχτό και η Ansible μπαίνει κανονικά!"
        }
        failure {
            echo "❌ FAILURE: Δες το Console Output. Μάλλον το κλειδί ή το host.yaml έχει θέμα."
        }
    }
}