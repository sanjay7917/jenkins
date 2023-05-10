pipeline {
    agent any
    stages {
        stage('code-pulling') {
            steps {
                git credentialsId: 'ubuntu', url: 'https://github.com/sanjay7917/student-ui.git'
            }
        }
        stage('build-maven') {
            steps {
                sh 'sudo apt update -y'
                sh 'sudo apt install maven -y'
                sh 'mvn clean package' 
            }
        }
        stage('SonarQube analysis') {
        //    def scannerHome = tool 'SonarScanner 4.8.0';
            steps{
                withSonarQubeEnv('sonarqube-9.9') { 
        // If you have configured more than one global server connection, you can specify its name
        //      sh "${scannerHome}/bin/sonar-scanner"
                sh "mvn sonar:sonar"
                }
            }
        }
        stage('artifact to s3') {
            steps {
                withAWS(credentials: 'aws', region: 'us-east-2') {
                     sh 'sudo apt update -y'
                     sh 'sudo apt install awscli -y'
                     sh 'aws s3 ls'
                     sh 'aws s3 mb s3://chorbucket-builder --region us-east-2' 
                     sh 'sudo mv /home/ubuntu/JENKINHOME/workspace/tomcat/target/studentapp-2.2-SNAPSHOT.war /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war'
                     sh 'aws s3 cp /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war  s3://chorbucket-builder/' 
                }
            }        
        }
        stage('deploy-to-tomcat-server') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ubu', keyFileVariable: 'id_rsa', usernameVariable: 'tomcat')]) {
                    sh'''
                    sudo ssh -i ${id_rsa} -T -o StrictHostKeyChecking=no ubuntu@3.17.37.200<<EOF
                    sudo apt update -y
                    sudo apt install awscli -y
                    sudo apt install openjdk-11-jre -y
                    export AWS_ACCESS_KEY_ID=AKIAVTTFCSQQMYIZP6N6
                    export AWS_SECRET_ACCESS_KEY=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
                    export AWS_DEFAULT_REGION=us-east-2
                    aws s3 ls
                    aws s3 cp s3://chorbucket-builder/studentapp-2.2-SNAPSHOT${BUILD_ID}.war .
                    curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.74/bin/apache-tomcat-9.0.74.tar.gz
                    sudo tar -xzvf apache-tomcat-9.0.74.tar.gz -C /opt/
                    sudo chmod 777 /opt/apache-tomcat-9.0.74/*  && sudo chown ubuntu: /opt/apache-tomcat-9.0.74/*
                    sudo cp -rv studentapp-2.2-SNAPSHOT${BUILD_ID}.war studentapp-2.2-SNAPSHOT.war
                    sudo cp -rv studentapp-2.2-SNAPSHOT.war /opt/apache-tomcat-9.0.74/webapps/
                    sudo sh /opt/apache-tomcat-9.0.74/bin/startup.sh
                    '''
                }
            }
        }
    }
}