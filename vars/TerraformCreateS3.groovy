def call(Map config = [:]) {
    def tfDir = config.get('tfDir', 'terraform-script')
    def awsCreds = config.get('awsCreds', 'aws-creds')
    def region = config.get('region', 'ap-southeast-1')

    pipeline {
        agent any

        stages {
            stage('Init & Plan ') {
                steps {
                    dir(tfDir) {
                        withCredentials([
                            aws(
                                credentialsId: awsCreds,
                                accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                            )
                        ]) {
                            sh """
                                export AWS_DEFAULT_REGION=${region}
                                terraform init
                                terraform plan -out=tfplan
                            """
                        }
                    }
                }
            }

            stage('Apply') {
                steps {
                    dir(tfDir) 
                        withCredentials([
                            aws(
                                credentialsId: awsCreds,
                                accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                            )
                        ]) {
                        input message: 'Approve apply?'
                        sh """
                            export AWS_DEFAULT_REGION=${region}
                            terraform apply -auto-approve tfplan
                        """
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
