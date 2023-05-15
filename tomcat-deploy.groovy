pipeline {
    agent any
    stages{
        stage('deploy-to-tomcat-server') {
            steps {
                sh'''
                sudo apt update -y
                sudo apt-get install openjdk-11-jre -y
                sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.74/bin/apache-tomcat-9.0.74.tar.gz
                sudo tar -xzvf apache-tomcat-9.0.74.tar.gz -C /opt
                sudo wget https://s3-us-west-2.amazonaws.com/studentapi-cit/student.war -P /opt/apache-tomcat-9.0.74/webapps
                sudo wget https://s3-us-west-2.amazonaws.com/studentapi-cit/mysql-connector.jar -P /opt/apache-tomcat-9.0.74/lib
                sudo sed -i 's/8080/8000/g' /opt/apache-tomcat-9.0.74/conf/server.xml
                sudo sh /opt/apache-tomcat-9.0.74/bin/startup.sh
                '''
            }
        }
    }
}
