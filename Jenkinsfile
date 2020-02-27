pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building for branch: ' + env.BRANCH_NAME
                sh './runapp.sh build -x test'
            }
        }
    }
}
