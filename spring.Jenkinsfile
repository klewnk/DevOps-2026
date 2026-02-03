pipeline {
    agent any

    environment {
        // Λέμε στο Ansible να μην κολλήσει στην ερώτηση "Are you sure you want to connect?"
        ANSIBLE_HOST_KEY_CHECKING = 'False'
    }

    stages {
        stage('Checkout') {
            steps {
                // Κατεβάζει τον κώδικα από το Git (εκεί που είναι το spring.yaml)
                checkout scm
            }
        }

        stage('Prepare Inventory') {
            steps {
                // Φτιάχνουμε ένα αρχείο inventory για να ξέρει το Ansible πού να στοχεύσει.
                // Αντιστοιχούμε το όνομα 'devops_gcloud1' (που έχεις στο yaml) με τον τρόπο σύνδεσης.
                // Επειδή το /etc/hosts το ξέρει ήδη ως devops_gcloud1, δεν χρειάζεται IP εδώ.
                sh "echo '[devops_gcloud1]' > inventory.ini"
                sh "echo 'devops_gcloud1 ansible_user=kleonkola ansible_connection=ssh' >> inventory.ini"
            }
        }

        stage('Deploy with Ansible') {
            steps {
                // Τρέχουμε το playbook χρησιμοποιώντας το inventory που φτιάξαμε
                // Σιγουρέψου ότι το αρχείο λέγεται spring.yaml στο repo σου!
                sh 'ansible-playbook -i inventory.ini spring.yaml'
            }
        }
    }
}