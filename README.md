# Jenkins Pipeline for Monolithic Application Deployment
## Deployment of a monolithic application on Tomcat by integrating several critical DevOps tools. 

🔹 GitHub – Version control at its finest! All code changes are tracked and managed effectively to ensure collaboration and a single source of truth.
🔹 Jenkins – Automates the CI/CD process to trigger builds, tests, and deployments, reducing human error and saving time.
🔹 SonarQube – Ensures high code quality by performing static code analysis and identifying potential issues early in the lifecycle.
🔹 Maven – Efficiently handles project dependencies and automates the build process, ensuring the application is always packaged correctly.
🔹 Nexus – Manages artifacts and stores built packages in a repository, ensuring seamless artifact versioning and availability for deployments.
🔹 Tomcat – A lightweight, powerful web server that provides the environment required for running Java-based web applications.

## Why Implement This Setup?

1️⃣ Speed and Efficiency: Automating code builds, testing, and deployment with Jenkins eliminates manual interventions and significantly reduces time-to-market.
2️⃣ Consistent Quality: SonarQube ensures that every code push meets defined quality standards, preventing production defects.
3️⃣ Dependency Management: Maven handles library conflicts efficiently, ensuring all necessary components are in sync.
4️⃣ Artifact Management: Nexus allows smooth versioning and reuse of build artifacts, avoiding repetitive work.
5️⃣ Scalable Environment: Tomcat enables efficient hosting for the monolithic application while supporting future enhancements.

## How This Implementation Improves Performance:

**Reduced Downtime**: Automated deployments via Jenkins reduce the time needed for production releases.

**Early Issue Detection**: SonarQube catches potential issues in code during development, preventing last-minute bottlenecks.

**Faster Rollbacks**: Nexus-managed artifacts make rollbacks easy in case of any failures.

**Streamlined Builds**: Maven ensures each build is reproducible and consistent, avoiding dependency conflicts.



## Step 1: Provision EC2 Instances

1. Launch 4 EC2 instances using the **Amazon Linux 2 AMI** with the following configuration:
   - **Instance Type**: `t2.medium`
   - **Security Groups**: Open necessary ports (e.g., 8080 for Jenkins, 9000 for SonarQube, etc.).
   - **Key Pair**: Use a secure SSH key pair for remote access.

2. Label each instance:
   - Instance 1: **Jenkins**
   - Instance 2: **SonarQube**
   - Instance 3: **Nexus**
   - Instance 4: **Tomcat**

---

## Step 2: Configure Jenkins (Server 1)

1. **Install Dependencies**:
   - Install **Java 17**:
     ```bash
     sudo amazon-linux-extras enable java-openjdk17
     sudo yum install java-17-openjdk -y
     ```
   - Install Jenkins:
     ```bash
     sudo yum update -y
     sudo yum install wget -y
     wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
     rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
     sudo yum install jenkins -y
     sudo systemctl enable jenkins
     sudo systemctl start jenkins
     ```
   - Install **Git**:
     ```bash
     sudo yum install git -y
     ```
   - Install **Maven with Java 11**:
     ```bash
     sudo amazon-linux-extras enable java-openjdk11
     sudo yum install java-11-openjdk-devel -y
     sudo yum install maven -y
     ```
   - Set up environment for Maven:
     ```bash
     sudo alternatives --config java
     ```

2. **Configure Jenkins**:
   - Access Jenkins at `http://<Jenkins-Instance-IP>:8080`.
   - Retrieve the initial admin password:
     ```bash
     cat /var/lib/jenkins/secrets/initialAdminPassword
     ```
   - Install required plugins (e.g., **SonarQube Scanner**, **Nexus Artifact Uploader**, etc.).
   - Configure system settings.

---

## Step 3: Configure SonarQube (Server 2)

1. **Install SonarQube**:
   - Install **Java 11**:
     ```bash
     sudo yum install java-11-openjdk -y
     ```
   - Download and extract SonarQube:
     ```bash
     wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-<VERSION>.zip
     unzip sonarqube-<VERSION>.zip
     ```
   - Start SonarQube:
     ```bash
     cd sonarqube-<VERSION>/bin/linux-x86-64
     ./sonar.sh start
     ```

2. **Configure SonarQube**:
   - Access SonarQube at `http://<SonarQube-Instance-IP>:9000`.
   - Log in with default credentials (`admin/admin`).
   - Generate a **security token** for Jenkins integration.

3. **Integrate with Jenkins**:
   - Add SonarQube server details in Jenkins under **Manage Jenkins > Configure System > SonarQube servers**.

---

## Step 4: Configure Nexus (Server 3)

1. **Install Nexus**:
   - Install **Java 11**:
     ```bash
     sudo yum install java-11-openjdk -y
     ```
   - Download and extract Nexus:
     ```bash
     wget https://download.sonatype.com/nexus/3/nexus-<VERSION>-unix.tar.gz
     tar -zxvf nexus-<VERSION>-unix.tar.gz
     ```
   - Run Nexus as a service.

2. **Configure Nexus**:
   - Access Nexus at `http://<Nexus-Instance-IP>:8081`.
   - Set up an admin user and configure repositories.

3. **Install Jenkins Plugin**:
   - Install the **Nexus Artifact Uploader** plugin in Jenkins.

---

## Step 5: Configure Tomcat (Server 4)

1. **Install Tomcat**:
   - Install **Java 11**:
     ```bash
     sudo yum install java-11-openjdk -y
     ```
   - Download and set up Tomcat:
     ```bash
     wget https://dlcdn.apache.org/tomcat/tomcat-<VERSION>/v<VERSION>/bin/apache-tomcat-<VERSION>.tar.gz
     tar -zxvf apache-tomcat-<VERSION>.tar.gz
     ```
   - Create **tomup** and **tomdown** scripts:
     ```bash
     ln -s <path-to-startup-script> /usr/local/bin/tomup
     ln -s <path-to-shutdown-script> /usr/local/bin/tomdown
     ```

2. **Configure Tomcat User**:
   - Add Jenkins user to Tomcat and grant sudo privileges:
     ```bash
     sudo useradd jenkins
     sudo usermod -aG sudo jenkins
     ```
---

## Step 6: Modify `pom.xml` for SonarQube and Nexus

1. Update the `pom.xml` file in your GitHub repository:
   - Add SonarQube analysis settings.
   - Configure Maven deployment settings for Nexus.

---



## Jenkins Pipeline
  ![Jenkins Pipeline](/Images/project6-jenkins.png)

## SonarQube Static Code Analysis
  * ![image](https://github.com/user-attachments/assets/a8bac607-cebf-4c78-b041-2765734bf039)

## Nexus Artifact Upload
  * ![image](https://github.com/user-attachments/assets/a18f2634-d4d9-4628-b9a2-453b14846016)

## Deployment in Tomcat Server
  * ![image](https://github.com/user-attachments/assets/7cffee3b-6abc-46bb-8f78-621499ad5696)

## Web Application
  * ![image](https://github.com/user-attachments/assets/0d5d22ce-14ae-46ee-af4f-f4cf3d950fca)







