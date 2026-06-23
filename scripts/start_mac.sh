#!/usr/bin/env bash
set -euo pipefail

IMAGE=route-finder
CONTAINER=route-finder-app
PORT=8080

build=false
[[ "${1:-}" == "--build" ]] && build=true

if ! docker image inspect "$IMAGE" &>/dev/null || $build; then
  echo "Building image $IMAGE..."
  docker build -t "$IMAGE" "$(dirname "$0")/.."
fi

if docker ps -q -f name="$CONTAINER" | grep -q .; then
  echo "Container $CONTAINER is already running."
else
  docker rm -f "$CONTAINER" 2>/dev/null || true
  docker run -d --name "$CONTAINER" -p "${PORT}:8080" "$IMAGE"
  echo "Started at http://localhost:${PORT}"
fi
