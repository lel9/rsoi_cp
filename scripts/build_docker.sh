SERVICE_NAME=$1
PATH_TO_DOCKERFILE=$2

docker build -t $DOCKER_LOGIN/$SERVICE_NAME:latest -f $PATH_TO_DOCKERFILE/Dockerfile $2