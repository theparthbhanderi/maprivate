#!/bin/bash

echo "Starting FixPix Website..."
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ROOT_DIR="$( dirname "$SCRIPT_DIR" )"

if [ -d "$ROOT_DIR/apps/website" ]; then
    cd "$ROOT_DIR/apps/website"
else
    echo "Website directory not found at $ROOT_DIR/apps/website!"
    exit 1
fi

npm run dev
