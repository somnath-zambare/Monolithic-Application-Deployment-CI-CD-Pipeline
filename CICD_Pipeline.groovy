pipeline {
    agent any
    tools{
        maven 'maven-3.9'
    }

    stages {
        stage('Git Checkout') {
            steps {
               git branch: 'main', url: 'https://github.com/somnath-zambare/myweb.git'
            }
        }
        
        stage('Building Application')
        {
            steps
            {
                sh 'mvn clean package'
            }
        }
        stage('SonarQube Analysis')
        {
            steps
            {
                withSonarQubeEnv(installationName:'sonarqube',credentialsId: 'jenkins-sonar-token') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Nexus Upload')
        {
            steps
            {
                nexusArtifactUploader artifacts: [[artifactId: 'myweb', classifier: '', file: 'target/myweb-9.0.12.war', type: 'war']], credentialsId: 'nexus-cred', groupId: 'in.javahome', nexusUrl: '3.92.234.247:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-releases', version: '9.0.12'
            }
        }
        stage('Tomcat Deploy')
        {
            steps{
                sshagent(['tomcat-credentials']) {

                sh """
                  ssh -o StrictHostKeyChecking=no ubuntu@54.197.40.80 sudo rm -rf /var/tmp/*.war
                  ssh -o StrictHostKeyChecking=no ubuntu@54.197.40.80 sudo rm -rf /opt/tomcat-9/webapps/*.war
                  scp -o StrictHostKeyChecking=no target/*.war ubuntu@54.197.40.80:/var/tmp/
                  ssh -o StrictHostKeyChecking=no ubuntu@54.197.40.80 sudo cp /var/tmp/*war /opt/tomcat-9/webapps/
                  ssh -o StrictHostKeyChecking=no ubuntu@54.197.40.80 sudo tomdown
                  ssh -o StrictHostKeyChecking=no ubuntu@54.197.40.80 sudo tomup

                
                """
            }
            }
        }
    }
}
