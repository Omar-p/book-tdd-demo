pipeline {
  agent any

  stages {
    stage("Build") {
      steps {
        sh "./mvnw clean package"
      }
    }
  }

  post {
    success {
      archiveArtifacts 'target/*.jar'
      junit 'target/surefire-reports/TEST*.xml'
    }

    always {
      cleanWs()
    }
  }
}