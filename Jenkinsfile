pipeline {
    environment {
        AWS_DEFAULT_REGION = 'ap-southeast-1'
    }

    stages {
        stage('Setup Terraform') {
            steps {
                sh '''
                mkdir -p $HOME/bin
                curl -fsSL https://releases.hashicorp.com/terraform/1.7.0/terraform_1.7.0_linux_amd64.zip -o terraform.zip
                unzip terraform.zip -d $HOME/bin
                export PATH=$PATH:$HOME/bin
                terraform -version
                '''
            }
        }
        stage('Terraform Init') {
            steps {
                withCredentials([aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh 'terraform init'
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                withCredentials([aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh 'terraform plan -out=tfplan'
                }
            }
        }

        stage('Terraform Apply') {
            when { branch 'main' }
            steps {
                input message: "Approve apply?"
                withCredentials([aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh 'terraform apply -auto-approve tfplan'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
        }
    }
}
