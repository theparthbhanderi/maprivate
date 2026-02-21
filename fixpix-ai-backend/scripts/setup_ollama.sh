#!/bin/bash
# FixPix Ollama Setup

echo "Setting up FixPix Assistant (Ollama)..."

# Check if installed
if ! command -v ollama &> /dev/null; then
    echo "Ollama not found. Installing..."
    curl -fsSL https://ollama.com/install.sh | sh
else
    echo "Ollama is installed."
fi

# Start Server Background
echo "Starting Ollama Server..."
ollama serve > ollama.log 2>&1 &
PID=$!
echo "Ollama running at PID $PID"

# Wait for startup
sleep 5

# Pull LLaMA 3
echo "Pulling LLaMA-3 model (this may take time)..."
ollama pull llama3

echo "✅ Ollama Ready!"
echo "Server: http://localhost:11434"
