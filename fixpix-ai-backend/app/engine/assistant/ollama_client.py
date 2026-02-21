import aiohttp
import logging
import json
from typing import List, Dict, Any, Generator, AsyncGenerator

logger = logging.getLogger(__name__)

class OllamaClient:
    """
    Client for local Ollama inference server.
    Default: http://localhost:11434
    """
    
    def __init__(self, base_url: str = "http://localhost:11434", model: str = "llama3"):
        self.base_url = base_url
        self.model = model
        
    async def chat(self, messages: List[Dict[str, str]], tools: List[Dict] = None, stream: bool = False) -> Dict:
        """
        Send chat request to Ollama.
        """
        url = f"{self.base_url}/api/chat"
        
        payload = {
            "model": self.model,
            "messages": messages,
            "stream": stream
        }
        
        if tools:
            # Check if model supports tools (Llama3 does via some tricks or official support depending on version)
            # For standard Ollama, we might need raw prompting if tools not fully supported in API yet.
            # But recent Ollama supports 'tools' param compatible with similar specs.
            # If not, we fall back to system prompt engineering.
            # For now, let's assume we pass in payload if supported, or inject into system prompt.
            # We will use System Prompt Injection for max compatibility.
             pass 

        try:
            async with aiohttp.ClientSession() as session:
                async with session.post(url, json=payload, timeout=60) as response:
                    if response.status != 200:
                        text = await response.text()
                        logger.error(f"Ollama Error {response.status}: {text}")
                        return {"error": f"Ollama Error: {text}"}
                        
                    if stream:
                        # Handle streaming response if needed (not implemented in this simplified client)
                         return {}
                    else:
                        return await response.json()
                        
        except Exception as e:
            logger.error(f"Failed to connect to Ollama at {self.base_url}: {e}")
            return {"error": "Ollama Unreachable", "mock": True}

    async def generate_mock(self, messages: List[Dict[str, str]]) -> Dict:
        """
        Mock response if Ollama is offline.
        """
        last_msg = messages[-1]["content"].lower()
        if "restore" in last_msg or "fix" in last_msg:
             return {
                 "message": {
                     "role": "assistant",
                     "content": "I can help with that! I'm activating the Face Restoration tool now.",
                     "tool_calls": [
                         {"function": {"name": "restore_face", "arguments": "{}"}}
                     ]
                 }
             }
        
        return {
            "message": {
                "role": "assistant",
                "content": "I am the FixPix AI Assistant (Mock Mode). It seems the Ollama server is not running, but I can still pretend to help! Try asking me to restore an image."
            }
        }
