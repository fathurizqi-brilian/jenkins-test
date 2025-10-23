def call(Map config = [:]) {
    def tfDir = config.get('tfDir', 'terraform-script')
    def awsCreds = config.get('awsCreds', 'aws-creds')
    def region = config.get('region', 'ap-southeast-1')

    pipeline {
        agent any

        environment {
            AWS_DEFAULT_REGION = config.get('region', 'ap-southeast-1')
        }

        stages {
            stage('Init') {
                steps {
                    dir(tfDir) {
                    withCredentials([aws(credentialsId: config.get('awsCreds', 'aws-creds'),
                                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        sh 'terraform init'
                    }
                }
            }

            stage('Plan') {
                steps {
                    dir(tfDir) {
                    withCredentials([aws(credentialsId: config.get('awsCreds', 'aws-creds'),
                                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        sh 'terraform plan -out=tfplan'
                    }
                }
            }

            stage('Apply') {
                when { branch 'main' }
                steps {
                    dir(tfDir) {
                    input message: 'Approve apply?'
                    withCredentials([aws(credentialsId: config.get('awsCreds', 'aws-creds'),
                                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
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
}
