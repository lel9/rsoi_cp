PATH_TO_FOLDER=$1
dir = $(pwd)
cd $1
npm run-script build
cd dir