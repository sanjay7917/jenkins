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
        stage("test-maven"){
            steps{
                sh 'mvn test'
                sh 'mvn clean package' 
                // sh 'sudo apt update -y'
                // sh 'sudo apt install maven -y'
                // sh 'mvn clean package' 
            }    
        }
        stage('artifact to s3') {
            steps {
                withAWS(credentials: 'aws', region: 'us-east-2') {
                     sh'''
                     sudo apt update -y
                     sudo apt install awscli -y
                     aws s3 ls
                     aws s3 mb s3://buck12312344 --region us-east-2
                     sudo mv /home/ubuntu/JENKINHOME/workspace/pull/target/studentapp-2.2-SNAPSHOT.war /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war
                     aws s3 cp /tmp/studentapp-2.2-SNAPSHOT${BUILD_ID}.war  s3://buck12312344/
                    '''
                }
            }     
        }
        // stage("deploy-tomcat"){
        //     steps{
        //         withCredentials([sshUserPrivateKey(credentialsId: 'ubuntu', keyFileVariable: 'id_rsa', usernameVariable: 'ubuntu')]) {
        //             sh'''
        //             sudo apt update -y
        //             sudo apt-get install openjdk-11-jre -y
        //             sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.74/bin/apache-tomcat-9.0.74.tar.gz
        //             sudo tar -xzvf apache-tomcat-9.0.74.tar.gz -C /opt
        //             sudo cp studentapp-2.2-SNAPSHOT.war /opt/apache-tomcat-9.0.74/webapps/
        //             sudo sh /opt/apache-tomcat-9.0.74/bin/startup.sh
        //             '''
        //         }
        //     }    
        // }
    }
}
