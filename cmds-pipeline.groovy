pipeline{
    agent any
    stages{
        stage("echoing string"){
            steps{
                echo 'Syntax Check'
            }
        }
        stage("shing one cmd"){
            steps{
                sh 'pwd'
            }
        }
        stage("shing multiple cmd"){
            steps{
                sh '''pwd
                ls
                date 
                cal 2222'''
            }
        }
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}