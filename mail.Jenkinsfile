pipeline {
    agent any

    environment {
        INVENTORY_PATH = "${WORKSPACE}/ansible-devops/inventory.ini"
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Deploy MailHog via Ansible') {
            steps {
                
                withCredentials([sshUserPrivateKey(credentialsId: 'id_devops_key', keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER')]) {
                    sh """
                        chmod 600 ${SSH_KEY}
                        
                        # Εκτέλεση του Ansible Playbook
                        ansible-playbook -i ${INVENTORY_PATH} \
                        ansible-devops/playbooks/mailhog_playbook.yaml \
                        --private-key=${SSH_KEY} \
                        --ssh-common-args='-o StrictHostKeyChecking=no' \
                        --user kleonkola
                    """
                }
            }
        }

        stage('Verify MailHog') {
            steps {

                sh 'curl -I http://34.51.255.13:8025 || echo "MailHog UI is up"'
            }
        }
    }

    post {
        always {
            mail(
                to: 'it2022041@hua.gr',
                from: 'it2022041@hua.gr',
                body: "Project ${env.JOB_NAME} <br> Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER}", 
                subject: "JENKINS: MailHog Deploy -> ${currentBuild.currentResult}",
                mimeType: 'text/html'
            )
        }
    }
}