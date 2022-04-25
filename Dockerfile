FROM amazoncorretto:8 AS build
WORKDIR /build

RUN yum install tar gzip -y

COPY settings.gradle build.gradle gradlew ./
COPY gradle gradle

# run to pull down the gradle wrapper and cache that on its own layer
RUN ./gradlew --version

COPY . .

ARG GIT_BRANCH
ARG GIT_TAG

# Build the application.
#
RUN ./scripts/build.sh
# Collect assembled jar for publishing
ARG BUILD_ARTIFACTS_JAVA=/build/build/libs/*.jar

# Generate Veracode Artifact
RUN tar -cvzf /java.tar.gz /build/build/libs/
ARG BUILD_ARTIFACTS_VERACODE=/java.tar.gz

# We only care about publishing a jar
FROM scratch
