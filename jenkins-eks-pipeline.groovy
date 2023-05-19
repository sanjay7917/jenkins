pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    parameters { 
        choice(name: 'EKS_AWS_REGION', description: 'Provide aws Region', choices: ['us-east-1', 'us-east-2', 'us-west-2', 'ap-east-1', 'ap-south-1', 'ap-northeast-2', 'ap-southeast-1', 'ap-southeast-2', 'ap-northeast-1', 'ca-central-1', 'eu-central-1', 'eu-west-1', 'eu-west-2', 'eu-west-3', 'eu-north-1', 'me-south-1', 'sa-east-1'])
        string(name: "EKS_CLUSTER_NAME", defaultValue: "", description: "Provide the name of cluster")
        choice(name: 'EKS_NODE_TYPE', description: 'Provide node type', choices: ['t2.medium', 't2.micro', 't2.small'])
        choice(name: 'EKS_NODE_COUNT', description: 'Provide the node Count', choices: ['1', '2', '3'])
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
                script {  
                    sh 'docker build -t aws_k8s_image .'
                }
            }    
        }        
        stage("push-docker-image"){
            steps{
                script {
                    sh 'docker tag aws_k8s_image sanjay7917/aws_k8s_image:V1'
                    withCredentials([string(credentialsId: 'dockpass', variable: 'dockpass')]) {
                        sh 'docker login -u sanjay7917 -p ${dockpass}'
                    }
                    sh 'docker push sanjay7917/aws_k8s_image:V1'
                }
            }
        }
        stage ('cluster-create'){
            agent {
                docker {
                    image 'sanjay7917/aws_k8s_image:V1'
                    reuseNode true
                }
            }
            steps {
                withAWS(credentials: 'aws', region: 'us-east-2') {
                    script {
                        sh 'eksctl create cluster --name ${EKS_CLUSTER_NAME} --region ${EKS_AWS_REGION} --node-type ${EKS_NODE_TYPE} --nodes ${EKS_NODE_COUNT}'
                        // sh 'eksctl delete cluster <Cluster_Name>' //Uncomment This For Deleting Cluster And Comment Above Command
                    }   
                }
            }
        }
    }
}
