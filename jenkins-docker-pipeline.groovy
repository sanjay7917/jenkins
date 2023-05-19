pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('code-pulling') {
            steps {
                git credentialsId: 'ubuntu', url: 'https://github.com/sanjay7917/student-ui.git'
            }
        }
        stage("build-maven"){
            steps{
                sh 'mvn clean package' 
            }    
        }
        stage("build-docker-image"){
            steps{
                //Note If we use Pipeline Script From We have to keep Dockerfile in Github repo and we don't have to move command to move Dockerfile into jenkins job workspace
                script {
                    sh 'mv /home/ubuntu/Dockerfile /var/lib/jenkins/workspace/docker'
                    sh 'docker build -t sanjay7917/studenttom .'
                }
            }    
        }        
        stage("push-docker-image"){
            steps{
                script {
                    withCredentials([string(credentialsId: 'dockpass', variable: 'dockpass')]) {
                        sh 'docker login -u sanjay7917 -p ${dockpass}'
                    }
                    sh 'docker push sanjay7917/studenttom'
                }
            }    
        }
    }
}
