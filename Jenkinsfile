pipeline {
    agent any
    stages {
        stage("Build source code") {
            steps {
                echo 'Start build'
                sh "chmod -R 755 ./gradlew"
                sh "./gradlew clean build"
            }
        }
        stage("Stop current project") {
            steps {
                echo 'Stopping current instance'
                sh "docker stop topwines || true"
                sh "docker rm topwines || true"
                sh "docker rmi topwines || true"
                echo 'Stopped current instance'
            }
        }
        stage("Build docker image") {
            steps {
                echo 'Start build docker image'
                sh "docker build -f src/main/docker/Dockerfile.jvm -t topwines ."
                echo 'Build image success'
            }
        }
        stage("Start application") {
            steps {
                echo 'Starting application'
                sh "docker run -d -p 8082:8082 --net=host --name=topwines topwines:latest"
                echo 'Start application success'
            }
        }
    }
}