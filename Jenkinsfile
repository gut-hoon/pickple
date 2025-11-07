pipeline {
    agent any

    environment {
        DEPLOY_DIR = '/home/ubuntu/pickple'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from Git...'
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                echo 'Checking Docker environment...'
                sh 'docker --version'
                sh 'docker compose version'
            }
        }

        stage('Sync to Production') {
            steps {
                echo 'Syncing files to production directory...'
                sh """
                    mkdir -p ${DEPLOY_DIR}
                    rsync -av --exclude '.git' --exclude '.env' ${WORKSPACE}/ ${DEPLOY_DIR}/
                """
            }
        }

        stage('Build Services') {
            steps {
                echo 'Building Docker images...'
                sh """
                    cd ${DEPLOY_DIR}
                    docker compose -f ${DOCKER_COMPOSE_FILE} build frontend
                    docker compose -f ${DOCKER_COMPOSE_FILE} build backend
                """
            }
        }

        stage('Start Services') {
            steps {
                echo 'Starting services...'
                sh """
                    cd ${DEPLOY_DIR}
                    docker compose -f ${DOCKER_COMPOSE_FILE} up -d mysql redis
                    sleep 10
                    docker compose -f ${DOCKER_COMPOSE_FILE} up -d --force-recreate backend
                    sleep 5
                    docker compose -f ${DOCKER_COMPOSE_FILE} up -d --force-recreate frontend
                    docker compose -f ${DOCKER_COMPOSE_FILE} restart nginx
                """
            }
        }

        stage('Health Check') {
            steps {
                echo 'Running health check on backend...'
                script {
                    retry(5) {
                        sleep 10
                        sh """
                            docker exec pickple-backend curl -f http://localhost:8080/actuator/health || exit 1
                        """
                    }
                }
                echo 'Health check passed!'
            }
        }

        stage('Cleanup Old Images') {
            steps {
                echo 'Removing unused Docker images...'
                sh """
                    docker image prune -f || true
                """
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying final deployment status...'
                sh """
                    cd ${DEPLOY_DIR}
                    docker compose ps
                """
            }
        }
    }

    post {
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
            sh """
                cd ${DEPLOY_DIR}
                docker compose logs --tail=100
            """
        }
    }
}