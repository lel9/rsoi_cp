#!/bin/bash

chmod +x "$1/gradlew"
"$1/gradlew" -b "$1/build.gradle" unpack