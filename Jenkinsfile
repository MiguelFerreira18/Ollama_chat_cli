def execCommand(command){
    if(isUnix()){
        sh command
    }else{
        bat command
    }
}

pipeline{
    agent any

    parameters {
        string(name: 'INSTALL_PATH', defaultValue: '', description: 'Installation Path')
    }

    tools{
        jdk 'java_21'
    }

    stages {
        stage('Checkout'){
            steps{
                checkout scm
            }
        }
        stage('Build jar'){
            steps{
                execCommand('mvn clean package')
            }
        }
        stage('Move jar into defined path'){
            steps{
                if(!params.INSTALL_PATH?.trim()){
                    error "INSTALL_PATH parameter is required!"
                }
                def jarFile = sh(script: "ls target/*_shade.jar", returnStdout: true).trim()
                execCommand("mv ${jarFile} ${params.INSTALL_PATH}")
            }
        }
    }
}