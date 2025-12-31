pipeline {
    agent any
    
    tools {
        maven 'Maven_3.8'
        jdk 'JDK_11'
    }
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main',
                    url: 'https://github.com/ENSAM/finance-refactoring.git'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: 'src/test*'
                    )
                }
            }
        }
        
        stage('Code Coverage') {
            steps {
                echo 'Generating code coverage report...'
                sh 'mvn jacoco:report'
            }
        }
        
        stage('Code Quality Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Waiting for Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
            emailext (
                subject: "Build ${currentBuild.result}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <p>Build Status: ${currentBuild.result}</p>
                    <p>Project: ${env.JOB_NAME}</p>
                    <p>Build Number: ${env.BUILD_NUMBER}</p>
                    <p>Build URL: ${env.BUILD_URL}</p>
                    <p>Code Coverage: Check JaCoCo report</p>
                    <p>SonarQube: ${SONAR_HOST_URL}</p>
                """,
                to: 'professor@university.edu',
                attachLog: true,
                mimeType: 'text/html'
            )
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
        unstable {
            echo 'Build is unstable!'
        }
    }
}
