@Library('jenkins-shared-lib') _

SayHello("Fathurizqi Azhari")

    pipeline {
        agent any

        stages {
            TerraformPlan(region: 'ap-southeast-1', awsCreds: 'aws-creds')
            TerraformApply(region: 'ap-southeast-1', awsCreds: 'aws-creds')
        }
        post {
            always {
                archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
            }
        }
    }