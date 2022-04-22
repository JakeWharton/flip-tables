FROM amazoncorretto:11 AS build

WORKDIR /build

RUN yum install tar gzip -y

COPY settings.gradle build.gradle gradlew ./
COPY gradle gradle

# run to pull down the gradle wrapper and cache that on its own layer
RUN ./gradlew --version

COPY . .

#
# Build the application.
#
RUN ./gradlew --no-daemon build

# Generate Veracode Artifact
RUN tar -cvzf /java.tar.gz /build/build/libs/
ARG BUILD_ARTIFACTS_VERACODE=/java.tar.gz

ARG BUILD_ARTIFACTS_JAVA=/build/build/libs/*.jar
ARG BUILD_ARTIFACTS_TEST_REPORTS=/build/build/test-results/test/TEST-*.xml
