pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'ap-southeast-1'
        TERRAFORM_VERSION = '1.7.0'
        TERRAFORM_PATH = "${WORKSPACE}/bin"
        PATH = "${WORKSPACE}/bin:${PATH}"
    }

    stages {
        stage('Setup Terraform') {
            steps {
                sh '''
                mkdir -p $TERRAFORM_PATH
                if [ ! -f "$TERRAFORM_PATH/terraform" ]; then
                    echo "Downloading Terraform ${TERRAFORM_VERSION}..."
                    curl -fsSL https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip -o terraform.zip
                    unzip -o terraform.zip -d $TERRAFORM_PATH
                    rm terraform.zip
                fi
                terraform -version
                '''
            }
        }

        stage('Init') {
            steps {
                sh 'terraform init'
            }
        }

        stage('Plan') {
            steps {
                sh 'terraform plan -out=tfplan'
            }
        }

        stage('Apply') {
            when { branch 'main' }
            steps {
                input message: 'Approve apply?'
                sh 'terraform apply -auto-approve tfplan'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
        }
    }
}
