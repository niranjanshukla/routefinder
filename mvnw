#!/bin/sh
# Minimal Maven bootstrap — downloads Maven 3.9.9 once, then delegates.
set -e

MVN_VERSION="3.9.9"
DIST_DIR="${HOME}/.m2/wrapper/dists/apache-maven-${MVN_VERSION}"
DIST_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MVN_VERSION}/apache-maven-${MVN_VERSION}-bin.tar.gz"

if [ ! -f "${DIST_DIR}/bin/mvn" ]; then
    echo "Downloading Apache Maven ${MVN_VERSION}..."
    mkdir -p "${DIST_DIR}"
    curl -fsSL "${DIST_URL}" | tar -xz -C "${DIST_DIR}" --strip-components=1
fi

exec "${DIST_DIR}/bin/mvn" "$@"
