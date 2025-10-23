pipeline {
    agent {
        docker {
            image 'hashicorp/terraform:1.7.0'
        }
    }
    stages {
        stage('Init') {
            steps { sh 'terraform init' }
        }
        stage('Plan') {
            steps { sh 'terraform plan' }
        }
    }
    stages {
        stage('Init') {
            steps { sh 'terraform init' }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
        }
    }
}