#!/bin/bash


BUILD_CMD="./gradlew --console=plain --no-daemon clean assemble"

if [ "${GIT_TAG}" ]
then
  RELEASE_VERSION=${GIT_TAG}-onecloud
	echo "Detected tagged build version ${GIT_TAG}. Setting version to ${RELEASE_VERSION}"
	BUILD_CMD+=" -Dorg.gradle.project.version=${RELEASE_VERSION}"
else
	echo "No tag detected on the current commit. This is a non-release build."
fi

echo "Executing build as: ${BUILD_CMD}"
exit=0
eval "${BUILD_CMD}" || exit=$?
exit $exit
