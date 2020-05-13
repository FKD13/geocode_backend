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
                sh 'sudo cp build/libs/*.war /home/groep29/backend/development/app.war'
                sh 'sudo systemctl restart backend_dev.service'
            }
        }
        stage('Deploy-Production') {
            when {
                branch 'master'
            }
            steps {
                echo 'Building War file for production ...'
                sh './runapp.sh bootWar'
                sh 'sudo cp build/libs/*.war /home/groep29/backend/production/app.war'
                sh 'sudo systemctl restart backend_prod.service'
            }
        }
        stage('Self Destruct') {
            steps {
                sh 'rm -rf .[!.]* *'
            }
        }
    }
    post {
        failure {
            sh 'rm -rf .[!.]* *'
        }
    }
}
