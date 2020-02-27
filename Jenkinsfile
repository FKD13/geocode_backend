pipeline {
    agent any

    node {
        stage('Build') {
            echo 'Building for Branch: ' + env.BRANCH_NAME
            sh './runapp.sh build -x test'
        }

        stage('Test') {
            echo 'Testing for branch: ' + env.BRANCH_NAME
            sh './runapp test'
        }
    }
}
