pipeline {
    agent{
        label "master"
    }
    tools {
        maven 'Maven'
    }
    stages {
        stage('code-pulling') {
            steps {
                slackSend channel: 'noso', message: 'Job Started'
                git credentialsId: 'ubuntu', url: 'https://github.com/sanjay7917/student-ui.git'
                slackSend channel: 'noso', message: 'Code Pulled'
            }
        }
        stage("build-maven"){
            steps{
                sh 'mvn clean package' 
                slackSend channel: 'noso', message: 'Build Successful'
            }    
        }
        stage('sonarqube-integration'){
            steps{
                withSonarQubeEnv('sonarqube-9.9') { 
                    sh "mvn sonar:sonar"
                    slackSend channel: 'noso', message: 'SonarQube Integrated'
                }
            }
        }
        stage('artifact-to-s3') {
            steps {
                withAWS(credentials: 'aws', region: 'us-east-2') {
                     sh'''
                     sudo apt update -y
                     sudo apt install awscli -y
                     aws s3 ls
                     aws s3 mb s3://buck12312344 --region us-east-2
                     sudo mv /var/lib/jenkins/workspace/deploy/target/studentapp-2.2-SNAPSHOT.war /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war
                     aws s3 cp /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war  s3://buck12312344/
                    '''
                    slackSend channel: 'noso', message: 'Artifact Stored in AWS S3'
                }
            }     
        }
        stage('deploy-to-tomcat-server') {
        //If You Dont Want to take SSH then create jenkins node and run this stage on that NODE Using agent.
            // agent {
            //     label 'tomcat-node'
            // }
            input {
                message "Should we continue?"
                ok "Yes"
            }
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ubuntu', keyFileVariable: 'id_rsa', usernameVariable: 'tomcat')]) {
                    sh'''
                    sudo ssh -i ${id_rsa} -T -o StrictHostKeyChecking=no ubuntu@13.59.197.251<<EOF
                    sudo apt update -y
                    sudo apt install awscli -y
                    sudo apt install openjdk-11-jre -y
                    sudo mkdir /var/jenkins
                    sudo chown ubuntu:ubuntu /var/jenkins
                    export AWS_ACCESS_KEY_ID=AKIAVTTFCSQQMYIZP6N6
                    export AWS_SECRET_ACCESS_KEY=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
                    export AWS_DEFAULT_REGION=us-east-2
                    aws s3 ls
                    aws s3 cp s3://buck12312344/studentapp-2.2-SNAPSHOT${BUILD_ID}.war .
                    curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.74/bin/apache-tomcat-9.0.74.tar.gz
                    sudo tar -xzvf apache-tomcat-9.0.74.tar.gz -C /opt/                 
                    sudo cp -rv studentapp-2.2-SNAPSHOT${BUILD_ID}.war student.war
                    sudo cp -rv student.war /opt/apache-tomcat-9.0.74/webapps/
                    sudo sh /opt/apache-tomcat-9.0.74/bin/startup.sh
                    '''
                    slackSend channel: 'noso', message: 'Application Successfully Deployed on Production'
                }
            }
        }
    }
    post{
        always{
            echo "Test Pipeline"
        }
        success{
            slackSend channel: 'noso', message: 'Pipeline Executed Successfully'
        }
        failure{
            slackSend channel: 'noso', message: 'Pipeline Failed to Execute'
        }
    }
}
