PATH_TO_BUILDGRADLE=$1

chmod +x $PATH_TO_BUILDGRADLE/gradlew
$PATH_TO_BUILDGRADLE/gradlew -b $PATH_TO_BUILDGRADLE/build.gradle -Dorg.gradle.jvmargs="-Xms32m -Xmx100m -Xss32m -XX:ReservedCodeCacheSize=64m" test
