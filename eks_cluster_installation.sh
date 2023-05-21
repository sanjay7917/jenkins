#!/bin/bash
sudo apt update -y
sudo apt install unzip -y
sudo apt install curl -y
sudo apt install openjdk-11-jre -y
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
aws configure set aws_access_key_id AKIAVTTFCSQQDB36THT4
aws configure set aws_secret_access_key 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
aws configure set default.region us-east-2
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
kubectl version --short --client
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
eksctl version