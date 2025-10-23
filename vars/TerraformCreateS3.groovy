def call(Map config = [:]) {
    def tfDir = config.get('tfDir', 'terraform-script') // default now points to terraform-script
    def awsCreds = config.get('awsCreds', 'aws-creds')
    def region = config.get('region', 'ap-southeast-1')

    pipeline {
        agent any

        environment {
            AWS_DEFAULT_REGION = region
        }

        stages {
            stage('Init') {
                steps {
                    dir(tfDir) {
                        withCredentials([aws(credentialsId: awsCreds,
                                             accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                             secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                            sh 'terraform init'
                        }
                    }
                }
            }

            stage('Plan') {
                steps {
                    dir(tfDir) {
                        sh 'terraform plan -out=tfplan'
                    }
                }
            }

            stage('Apply') {
                when { branch 'main' }
                steps {
                    dir(tfDir) {
                        input message: 'Approve apply?'
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
