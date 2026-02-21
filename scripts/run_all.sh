#!/bin/bash

echo "Starting FixPix Full Stack..."

# Trap for cleanup
trap 'kill $(jobs -p)' EXIT

# Start Backend
./scripts/run_backend.sh &
PID_BACKEND=$!
echo "Backend started on PID $PID_BACKEND"

# Start Website
./scripts/run_website.sh &
PID_WEB=$!
echo "Website started on PID $PID_WEB"

echo "Full Stack is Running. Press Ctrl+C to stop."
wait
