pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        echo 'Test'
        sh 'cp .env.template .env'
        sh './runapp.sh build'
        echo 'Yeetskeet'
      }
    }
  }
}
