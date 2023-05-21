#!/bin/bash
sudo apt update -y
sudo apt install unzip -y
sudo apt install curl -y
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
aws configure set aws_access_key_id AKIAVTTFCSQQDB36THT4
aws configure set aws_secret_access_key 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
aws configure set default.region us-east-2

curl -o aws-iam-authenticator https://s3.us-west-2.amazonaws.com/amazon-eks/1.21.2/2021-07-05/bin/linux/amd64/aws-iam-authenticator
chmod +x ./aws-iam-authenticator
mkdir -p $HOME/bin && cp ./aws-iam-authenticator $HOME/bin/aws-iam-authenticator && export PATH=$PATH:$HOME/bin
echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc
aws-iam-authenticator help
#KUBECTL
curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.25.7/2023-03-17/bin/linux/amd64/kubectl
chmod +x ./kubectl
mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$PATH:$HOME/bin
echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc
kubectl version --short --client
#EKSCTL
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
eksctl version
eksctl create cluster --name eks-cluster --node-type t2.medium --nodes 1 --region us-east-2

aws sts get-caller-identity
aws --version
aws eks update-kubeconfig --region us-east-2 --name eks-cluster

#KUBECTL WAY 2
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
kubectl version --short --client


#jenkins slave 
adduser jenkins
mkdir /var/lib/jenkins
chown jenkins:jenkins /var/lib/jenkins

eksctl create cluster --name demo --region us-east-2 --nodegroup-name nodes --node-type t2.micro --managed --nodes 2
eksctl get cluster --name demo --region us-east-2
aws eks update-kubeconfig --name demo --region us-east-2
cat  /var/lib/jenkins/.kube/config
kubectl get nodes
eksctl delete cluster --name demo --region us-east-2