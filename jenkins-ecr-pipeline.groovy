pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        AWS_ACCOUNT_ID="385685296160"
        AWS_DEFAULT_REGION="us-east-2"
        IMAGE_REPO_NAME="aws_k8s_image"
        IMAGE_TAG="V1"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
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
        stage("build-docker-image-for-ecr"){
            steps{
                script {  
                    sh 'docker build -t ${IMAGE_REPO_NAME} .'
                }
            }    
        }
        stage("push-docker-image-to-ecr"){
            steps{
                script {
                    sh "docker tag ${IMAGE_REPO_NAME} ${REPOSITORY_URI}:${IMAGE_TAG}"
                    sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                    sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }
    }
}
