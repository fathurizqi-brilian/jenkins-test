def call(Map config = [:]) {
    pipeline {
        agent any

        environment {
            AWS_DEFAULT_REGION = config.get('region', 'ap-southeast-1')
        }

        stages {
            stage('Init') {
                steps {
                    withCredentials([aws(credentialsId: config.get('awsCreds', 'aws-creds'),
                                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        sh 'terraform init'
                    }
                }
            }

            stage('Plan') {
                steps {
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
