@Library('jenkins-shared-lib') _

SayHello("Fathurizqi Azhari")

    pipeline {
        agent {
            docker {
                image 'jenkins-aws-tf:latest'
                label 'agent2'
            }
        }
        // agent {
        //     label 'agent2'
        // }

        stages {
            stage('Init & Plan ') {
                steps {
                    TerraformPlan(region: 'ap-southeast-1', awsCreds: 'aws-creds')
                    echo "Terraform Plan Succeed"
                }
            }

            stage('Apply') {
                steps {
                    TerraformApply(region: 'ap-southeast-1', awsCreds: 'aws-creds')
                    echo "Terraform Apply Succeed"
                }
            }
        }

        post {
            always {
                archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
            }
        }
    }