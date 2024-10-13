pipeline {
    agent any
    stages {
        stage("code checkout") {
            steps {
                echo 'Cloning the repository'
                git branch: 'main', url: 'https://github.com/paritosh277/NAGP_QA_2024.git'
            }
        }
        stage("code build and unit test") {
            steps {
                echo 'Testing the code'
                bat 'mvn clean test'
            }
        }
        stage("Sonar Analysis") {
            steps {
                withSonarQubeEnv("Test_SonarQube")
                {
                    bat "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar" 
                }
            }
        }
		stage("Publish to Artifactory"){
            steps{
                rtMavenDeployer(
                    id: 'deployer',
                    serverId: '3154028@artifactory',
                    releaseRepo: 'nagp.session.2024',
                    snapshotRepo: 'nagp.session.2024'
                )
                rtMavenRun(
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: 'deployer'
                    )
                rtPublishBuildInfo(
                    serverId:'3154028@artifactory',
                )
            }  
		}
    }
    post {
        always {
            echo 'this is always going to execute, in case of failure as well'
        }
        success {
            echo 'build success'
        }
        failure {
            echo 'build failed'
        }
    }
}
