PATH_TO_FOLDER=$1
dir=$(pwd)
cd $PATH_TO_FOLDER
npm install
npm run-script build
cd $dir