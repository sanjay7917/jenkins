// // EKS Cluster USING DOCKER AGENT
// pipeline {
//     agent any
//     tools {
//         maven 'Maven'
//     }
//     parameters { 
//         choice(name: 'EKS_AWS_REGION', description: 'Provide aws Region', choices: ['us-east-1', 'us-east-2', 'us-west-2', 'ap-east-1', 'ap-south-1', 'ap-northeast-2', 'ap-southeast-1', 'ap-southeast-2', 'ap-northeast-1', 'ca-central-1', 'eu-central-1', 'eu-west-1', 'eu-west-2', 'eu-west-3', 'eu-north-1', 'me-south-1', 'sa-east-1'])
//         string(name: "EKS_CLUSTER_NAME", defaultValue: "", description: "Provide the name of cluster")
//         choice(name: 'EKS_NODE_TYPE', description: 'Provide node type', choices: ['t2.medium', 't2.micro', 't2.small'])
//         choice(name: 'EKS_NODE_COUNT', description: 'Provide the node Count', choices: ['1', '2', '3'])
//     }
//     stages {
//         stage('code-pulling') {
//             steps {
//                 git credentialsId: 'ubuntu', url: 'https://github.com/sanjay7917/student-ui.git'
//             }
//         }
//         stage("build-maven"){
//             steps{
//                 sh 'mvn clean package' 
//             }    
//         }
//         stage("build-docker-image"){
//             steps{
//                 script {  
//                     sh 'docker build -t aws_k8s_image .'
//                 }
//             }    
//         }        
//         stage("push-docker-image"){
//             steps{
//                 script {
//                     sh 'docker tag aws_k8s_image sanjay7917/aws_k8s_image:V1'
//                     withCredentials([string(credentialsId: 'dockpass', variable: 'dockpass')]) {
//                         sh 'docker login -u sanjay7917 -p ${dockpass}'
//                     }
//                     sh 'docker push sanjay7917/aws_k8s_image:V1'
//                 }
//             }
//         }
//         stage ('cluster-create'){
//             agent {
//                 docker {
//                     image 'sanjay7917/aws_k8s_image:V1'
//                     reuseNode true
//                 }
//             }
//             steps {
//                 withAWS(credentials: 'aws', region: 'us-east-2') {
//                     script {
//                         sh 'eksctl create cluster --name ${EKS_CLUSTER_NAME} --region ${EKS_AWS_REGION} --node-type ${EKS_NODE_TYPE} --nodes ${EKS_NODE_COUNT}'
//                         // sh 'eksctl delete cluster <Cluster_Name>' //Uncomment This For Deleting Cluster And Comment Above Command
//                     }   
//                 }
//             }
//         }
//     }
// }
// EKS Cluster USING SSH AGENT
pipeline {
    agent any
    stages {
        stage('code-pulling') {
            steps {
                git branch: 'main', credentialsId: 'ubuntu', url: 'https://github.com/sanjay7917/springboot-app.git'
            }
        }
        stage('copy') {
            steps {
                    sshagent(['ubuntu']) {
                        sh "scp -o StrictHostKeyChecking=no deploysvc.yml ubuntu@18.218.137.135:/home/ubuntu"
                }
            }
        }
        stage('kubectl') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ubuntu', keyFileVariable: 'id_rsa', usernameVariable: 'eks')]) {
                    sh'''
                    sudo ssh -i ${id_rsa} -T -o StrictHostKeyChecking=no ubuntu@18.218.137.135<<EOF
                    pwd
                    ls
                    kubectl version --short --client
                    eksctl create cluster --name=eksdemo1 \
                                        --region=us-east-2 \
                                        --zones=us-east-2a,us-east-2b \
                                        --without-nodegroup
                    eksctl utils associate-iam-oidc-provider \
                        --region us-east-2 \
                        --cluster eksdemo1 \
                        --approve
                    eksctl create nodegroup --cluster=eksdemo1 \
                                        --region=us-east-2 \
                                        --name=eksdemo1-ng-public1 \
                                        --node-type=t3.medium \
                                        --nodes=2 \
                                        --nodes-min=2 \
                                        --nodes-max=4 \
                                        --node-volume-size=20 \
                                        --ssh-access \
                                        --ssh-public-key=kube-demo \
                                        --managed \
                                        --asg-access \
                                        --external-dns-access \
                                        --full-ecr-access \
                                        --appmesh-access \
                                        --alb-ingress-access
                    eksctl get cluster
                    kubectl get nodes -o wide
                    kubectl apply -f deploysvc.yml
                    '''
                }
            }
        }
        // stage('Deploy App on k8s') {
        //     steps {    
        //         withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'K8S', namespace: '', restrictKubeConfigAccess: false, serverUrl: '') {
        //             sh 'sudo ssh -i ${id_rsa} -T -o StrictHostKeyChecking=no ubuntu@13.59.197.251<<EOF'
        //         }
        //             sshagent(['ubuntu']) {
        //                 sh "scp -o StrictHostKeyChecking=no deploysvc.yml ubuntu@18.218.170.237:/home/ubuntu"
        //                 script {
        //                     try{
        //                         sh "ssh ubuntu@18.218.170.237 kubectl apply -f ."
        //                     }catch(error){
        //                         sh "ssh ubuntu@18.218.170.237 kubectl create -f ."
        //                 }
        //             }
        //         }
        //     }
        // }
    }
}

