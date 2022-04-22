#!/bin/bash


BUILD_CMD="./gradlew --console=plain --no-daemon clean assemble"

if [ "${GIT_TAG}" ]
then
	echo "Detected a tagged build. Setting version to ${GIT_TAG}"
	BUILD_CMD+=" -Dorg.gradle.project.version=${GIT_TAG}"
else
	echo "No tag detected on the current commit. This is a non-release build."
fi

echo "Executing build as: ${BUILD_CMD}"
exit=0
eval ${BUILD_CMD} || exit=$?
exit $exit
