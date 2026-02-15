pipeline {
    agent any

    parameters {
        booleanParam(name: 'CHECK_CONN', defaultValue: true, description: 'Check connection to VM')
        booleanParam(name: 'INSTALL_POSTGRES', defaultValue: true, description: 'Install Native PostgreSQL')
        booleanParam(name: 'INSTALL_MAILHOG', defaultValue: true, description: 'Install Native MailHog')
        booleanParam(name: 'INSTALL_ADMINER', defaultValue: true, description: 'Install Native Adminer')
        booleanParam(name: 'INSTALL_SPRING_NGINX', defaultValue: true, description: 'Install Java App & Nginx')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/klewnk/DevOps-2026.git'
            }
        }

        stage('Ping VM') {
            when { expression { return params.CHECK_CONN } }
            steps {
                sshagent(['gcloud-ssh-key']) {
                    sh "ansible -i ansible-devops/inventory.ini all -m ping"
                }
            }
        }

        stage('Deploy Postgres') {
            when { expression { return params.INSTALL_POSTGRES } }
            steps {
                sshagent(['gcloud-ssh-key']) {
                    sh "ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/postgres.yaml"
                }
            }
        }

        stage('Deploy MailHog') {
            when { expression { return params.INSTALL_MAILHOG } }
            steps {
                sshagent(['gcloud-ssh-key']) {
                    sh "ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/mailhog.yaml"
                }
            }
        }

        stage('Deploy Adminer') {
            when { expression { return params.INSTALL_ADMINER } }
            steps {
                sshagent(['gcloud-ssh-key']) {
                    sh "ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/db_ui.yaml"
                }
            }
        }

        stage('Deploy App & Nginx') {
            when { expression { return params.INSTALL_SPRING_NGINX } }
            steps {
                sshagent(['gcloud-ssh-key']) {
                    sh "ansible-playbook -i ansible-devops/inventory.ini ansible-devops/playbooks/spring.yaml"
                }
            }
        }
    }

    post {
        success {
            echo '🚀 Όλα τα Native Services εγκαταστάθηκαν επιτυχώς!'
        }
        failure {
            echo '❌ Κάτι πήγε λάθος στην εγκατάσταση. Δες τα logs του Ansible.'
        }
    }
}