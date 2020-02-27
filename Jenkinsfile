pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building for branch: ' + env.BRANCH_NAME
                sh './runapp.sh build -x test'
            }
        }
        stage('Deploy-Development') {
            when {
                branch 'development'
            }
            steps {
                echo 'Building War file for development ...'
                sh './runapp.sh bootWar'
                echo 'Deploying should happen below'
                //TODO deploy to development
            }
        }
        stage('Deploy-Production') {
            when {
                branch 'master'
            }
            steps {
                echo 'Building War file for production ...'
                sh './runapp.sh bootWar'
                echo 'Deploying should happen below'
                //TODO deploy to production
            }
        }
    }
}
