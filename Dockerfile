# FROM tomcat 
# WORKDIR webapps 
# COPY target/studentapp-2.2-SNAPSHOT.war .
# RUN mv studentapp-2.2-SNAPSHOT.war student.war
# ENTRYPOINT ["sh", "/usr/local/tomcat/bin/startup.sh"]
# ===============================
# FROM ubuntu
# RUN apt update -y
# RUN apt install unzip -y && apt install curl -y
# RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
# RUN unzip awscliv2.zip
# RUN ./aws/install
# RUN aws configure set aws_access_key_id <Access_Key> && \
#     aws configure set aws_secret_access_key <Secret_Access_Key> && \
#     aws configure set default.region us-east-2
# RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
# RUN chmod +x kubectl
# RUN mv kubectl /usr/local/bin/
# RUN curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
# RUN mv /tmp/eksctl /usr/local/bin
# ===============================
# FROM ubuntu
# COPY aws_k8s_setup.sh /tmp
# RUN chmod +x /tmp/aws_k8s_setup.sh
# RUN sh /tmp/aws_k8s_setup.sh
# RUN rm -rvf /tmp/aws_k8s_setup.sh
# COPY k8s_manifest /tmp
# WORKDIR /tmp/k8s_manifest
# ===============================
# FROM tomcat 
# COPY aws_k8s_setup.sh /tmp
# RUN chmod +x /tmp/aws_k8s_setup.sh
# RUN sh /tmp/aws_k8s_setup.sh
# RUN rm -rvf /tmp/aws_k8s_setup.sh
# RUN aws s3 cp s3://buck12312344/studentapp-2.2-SNAPSHOT.war . 
# RUN mv studentapp-2.2-SNAPSHOT.war student.war
# RUN mv student.war /usr/local/tomcat/webapps/
# ENTRYPOINT ["sh", "/usr/local/tomcat/bin/startup.sh"]
# ===============================
FROM tomcat 
RUN rm -rvf /usr/local/tomcat/webapps
RUN mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps
COPY student.war /usr/local/tomcat/webapps/
RUN sed -i 's/8080/8000/g' /usr/local/tomcat/conf/server.xml
CMD /usr/local/tomcat/bin/startup.sh; sleep inf


