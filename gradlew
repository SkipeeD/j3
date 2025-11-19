#!/usr/bin/env bash
set -euo pipefail

DEFAULT_GRADLE_VERSION="8.7"
GRADLE_VERSION="${GRADLE_VERSION:-$DEFAULT_GRADLE_VERSION}"
DIST_NAME="gradle-${GRADLE_VERSION}"
DIST_DIR="${GRADLE_USER_HOME:-$PWD/.gradle-dist}"
DIST_ARCHIVE="$DIST_DIR/${DIST_NAME}-bin.zip"
GRADLE_HOME_DIR="$DIST_DIR/${DIST_NAME}"

ensure_command() {
    if ! command -v "$1" >/dev/null 2>&1; then
        echo "Error: $1 is required for this script." >&2
        exit 1
    fi
}

fetch_gradle() {
    mkdir -p "$DIST_DIR"
    local url="https://services.gradle.org/distributions/${DIST_NAME}-bin.zip"
    echo "Ensuring Gradle ${GRADLE_VERSION} is available..."
    if [ ! -f "$DIST_ARCHIVE" ]; then
        echo "Downloading ${url}"
        if command -v curl >/dev/null 2>&1; then
            curl -L "$url" -o "$DIST_ARCHIVE"
        else
            ensure_command wget
            wget -O "$DIST_ARCHIVE" "$url"
        fi
    fi
    if [ ! -d "$GRADLE_HOME_DIR" ]; then
        ensure_command unzip
        unzip -q "$DIST_ARCHIVE" -d "$DIST_DIR"
    fi
}

fetch_gradle

if [ -n "${GRADLE_JAVA_HOME:-}" ]; then
    export JAVA_HOME="$GRADLE_JAVA_HOME"
fi

exec "$GRADLE_HOME_DIR/bin/gradle" "$@"
