#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'todo-fe'
    String ecrUrl = '893473272543.dkr.ecr.us-east-1.amazonaws.com'
    String awsRegion = 'us-east-1'
    String clusterName = 'DevOpsEKScluster'
    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    // Checkout from GIT
                    sh 'git checkout main'
                    sh 'git pull'
                }
            }

            stage('Build docker image and Push to ECR') {
                steps {
                    // Build Docker Image for Application
                    withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                        sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrUrl}"
                        sh "docker build -t ${name} ."
                        sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
                        sh "docker push ${ecrUrl}/${name}:latest" 
                    }
                }
            }

            stage('Deploy') {
                steps {
                    withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                        sh "aws eks describe-cluster --region ${awsRegion} --name ${clusterName} --query cluster.status"
                        sh "aws eks --region ${awsRegion} update-kubeconfig --name ${clusterName}"
                        sh 'kubectl create ns eks-ns'
                        sh 'kubectl apply -f .cd/frontend.yaml'
                    }
                }
            }
        }
    } 
}