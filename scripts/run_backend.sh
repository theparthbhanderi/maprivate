#!/bin/bash

echo "Starting FixPix Backend..."
# Determine root directory (one level up from scripts/ if called as scripts/run_backend.sh)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ROOT_DIR="$( dirname "$SCRIPT_DIR" )"
cd "$ROOT_DIR/backend" || { echo "Backend directory not found at $ROOT_DIR/backend"; exit 1; }

# Activate venv if exists
if [ -d "venv" ]; then
    source venv/bin/activate
elif [ -d "../venv" ]; then
    source ../venv/bin/activate
else
    echo "Warning: No venv found. Running with system python."
fi

# Run
python3 manage.py runserver 0.0.0.0:8000
