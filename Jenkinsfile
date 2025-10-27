@Library('jenkins-shared-lib') _

SayHello("Fathurizqi Azhari")

    pipeline {
        // agent {
        //     docker {
        //         image 'sonarsource/sonar-scanner-cli:latest'
        //         label 'agent2'
        //     }
        // }
        agent {
            label 'agent2'
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