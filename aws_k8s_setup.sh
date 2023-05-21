#!/bin/bash
apt update -y
apt install unzip -y && apt install curl -y

#AWSCLI INSTALLATION
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
./aws/install

# #AWS CONFIURATION
# aws configure set aws_access_key_id <Access_Key>
# aws configure set aws_secret_access_key <Secret_Access_Key>
# aws configure set default.region us-east-2

#EKSCTL INSTALLATION
#FOR Latest VERSION OF KEUBECTL
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
mv kubectl /usr/local/bin/

#FOR Specefic VERSION OF KEUBECTL
# curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.25.9/2023-05-11/bin/linux/amd64/kubectl
# chmod +x ./kubectl
# mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$HOME/bin:$PATH

#EKSCTL INSTALLATION
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

#TOMCAT DEPLOYMENT
# aws s3 cp s3://buck12312344/studentapp-2.2-SNAPSHOT.war . 
# curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.74/bin/apache-tomcat-9.0.74.tar.gz
# tar -xzvf apache-tomcat-9.0.74.tar.gz -C /opt/  
# mv studentapp-2.2-SNAPSHOT.war student.war
# sed -i 's/8080/8000/g' /opt/apache-tomcat-9.0.74/conf/server.xml
# mv student.war /opt/apache-tomcat-9.0.74/webapps/
# mv student.war /usr/local/tomcat/webapps
# sh /opt/apache-tomcat-9.0.74/bin/shutdown.sh
# sh /opt/apache-tomcat-9.0.74/bin/startup.sh

# sed -i 's/8080/8000/g' /usr/local/tomcat/conf/server.xml 
# sh /usr/local/tomcat/bin/shutdown.sh
# sh /usr/local/tomcat/bin/startup.sh