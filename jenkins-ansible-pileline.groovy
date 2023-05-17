pipeline{
   agent{
        label "slave"
    }
   stages{
       stage("Ansible-Integration"){
           steps{
            ansiblePlaybook credentialsId: 'ubuntu', disableHostKeyChecking: true, installation: 'Ansible', inventory: '/etc/ansible/hosts', playbook: '/etc/ansible/tree.yml'
           }
       }
   }
}

