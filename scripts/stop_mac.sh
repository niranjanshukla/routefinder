#!/usr/bin/env bash
set -euo pipefail

CONTAINER=route-finder-app
docker stop "$CONTAINER" && docker rm "$CONTAINER"
echo "Stopped and removed $CONTAINER"
