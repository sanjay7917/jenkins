pipeline {
    agent any
    environment {
        name = 'KILLu'
    }
    parameters {
        string(name: 'Pirate' ,defaultValue: 'Luffy' ,description: "SUN GOD NIKA")
        booleanParam(name: 'Haki' ,defaultValue: true ,description: "")
        choice(name: 'Islands' ,choices: ['Wano', 'Egghead', 'Elbaf'] ,description: "")
    }
    stages {
        stage('Run Cmds') {
            steps {
                sh '''
                pwd
                date
                cal 2024
                '''
            }
        }
        stage('Environment') {
            environment {
                username = 'SILCO'
            }
            steps {
                sh 'echo "${BUILD_ID}"'
                sh 'echo "${name}"'
                sh 'echo "${username}"'
            }
        }
        stage('Parameters') {
            steps {
                echo 'Deployinggggggg To Testtttttt'
                sh 'echo "${name}"'
                sh 'echo "${Pirate}"'
                sh 'echo "${Haki}"'
                sh 'echo "${Islands}"'
            }
        }
        stage('InputFromUser') {
            input {
                message "Would U Like To Continue?"
                ok "YES"
            }
            steps {
                echo 'Input From User'
            }
        }
        stage('Deploy to Prod') {
            steps {
                echo 'Deployinggggggg To Proddddddd'
            }
        }
    }
    post{
        always{
            echo 'I WILL ALWAYS RUN'
        }
        failure{
            echo 'FAILURE'
        }
        success{
            echo 'SUCCESS'
        }
    }
}
