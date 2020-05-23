PATH_TO_BUILDGRADLE=$1

chmod +x "PATH_TO_BUILDGRADLE/gradlew"
"PATH_TO_BUILDGRADLE/gradlew" -b "PATH_TO_BUILDGRADLE/build.gradle" unpack
