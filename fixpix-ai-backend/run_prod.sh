#!/bin/bash

# Ensure models are present
echo "Checking model weights..."
./venv/bin/python scripts/download_models.py

# Start Server
echo "Starting FixPix AI Backend..."
./venv/bin/python -m uvicorn main:app --host 0.0.0.0 --port 8001 --workers 1 --loop uvloop
