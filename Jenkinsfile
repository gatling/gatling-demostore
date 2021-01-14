def branch = env.CHANGE_BRANCH ? env.CHANGE_BRANCH : env.BRANCH_NAME

pipeline {
  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: jenkins
spec:
  containers:
  - name: docker
    image: eu.gcr.io/production-243513/docker-gcr:18.09
    command:
     - tail
     - -f
     - /etc/passwd
    env:
      - name: DOCKER_HOST
        value: "tcp://docker.jenkins-ci.svc.cluster.local:2375"
    volumeMounts:
      - name: docker-gcr-config
        mountPath: /home/jenkins/.docker
  volumes:
    - name: docker-gcr-config
      configMap:
        name: docker-gcr-config
"""
    }
  }

  stages {
    stage('Build Docker image') {
      steps {
        container('docker') {
          script {
            docker.build('eu.gcr.io/production-243513/demostore:' + branch).push()
          }
        }
      }
    }
  }
}
