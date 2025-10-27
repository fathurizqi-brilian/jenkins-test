@Library('jenkins-shared-lib') _

SayHello("Fathurizqi Azhari")

    pipeline {
        agent {
            docker {
                image 'hashicorp/terraform:light'
                args '--entrypoint="" -v /var/run/docker.sock:/var/run/docker.sock'
                label 'agent2-terraform'
            }
        }

        stages {
            stage('Init & Plan ') {
                steps {
                    TerraformPlan(region: 'ap-southeast-1', awsCreds: 'aws-creds')
                }
            }

            stage('Apply') {
                steps {
                    TerraformApply(region: 'ap-southeast-1', awsCreds: 'aws-creds')
                }
            }
        }

        post {
            always {
                archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
            }
        }
    }