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
    }

    always {
      junit 'target/surefire-reports/*.xml'
      cleanWs()
    }
  }
}