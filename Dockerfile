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
# RUN aws configure set aws_access_key_id AKIAVTTFCSQQMYIZP6N6 && \
#     aws configure set aws_secret_access_key 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 && \
#     aws configure set default.region us-east-2
# RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
# RUN chmod +x kubectl
# RUN mv kubectl /usr/local/bin/
# RUN curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
# RUN mv /tmp/eksctl /usr/local/bin
# ===============================
FROM ubuntu
COPY aws_k8s_setup.sh /tmp
RUN chmod +x /tmp/aws_k8s_setup.sh
RUN sh /tmp/aws_k8s_setup.sh
RUN rm -rvf aws_k8s_setup.sh
