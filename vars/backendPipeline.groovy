#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'backend'
    String ecrUrl = '893473272543.dkr.ecr.us-east-1.amazonaws.com'
    String awsRegion = 'us-east-1'
    String clusterName = 'eks-demo'
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

            stage('Neymar') {
                steps {
                    // Install project dependencies using npm
                    echo "MSA cong chien test 14"
                }
            }

            stage('Build Docker Image') {
                steps {
                    sh "docker build -t ${name} ."
                    //docker.withRegistry('https://893473272543.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:aws-credentials')
                    // Build Docker Image for Application
                    // withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                    //     sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrUrl}"
                    //     sh "docker build -t ${name} ."
                    //     sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
                    //     sh "docker push ${ecrUrl}/${name}:latest" 
                    // }
                }
            }

            // stage('Deploy') {
            //     steps {
            //         withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
            //             withKubeConfig([credentialsId: 'eks-credentials']) {
            //                 sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
            //                 sh 'chmod u+x ./kubectl'
            //                 sh "./kubectl config set-context --current --namespace eks-ns"
            //                 sh "aws eks describe-cluster --region ${awsRegion} --name ${clusterName} --query cluster.status"
            //                 sh "aws eks --region ${awsRegion} update-kubeconfig --name ${clusterName}"
            //                 sh "./kubectl rollout restart deploy ${name}"
            //             }
            //         }
            //     }
            // }
        }
    } 
}