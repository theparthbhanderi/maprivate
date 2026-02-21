import aiohttp
import logging
import json
import asyncio
from typing import List, Dict, Any, AsyncGenerator

logger = logging.getLogger(__name__)

class LLMClient:
    """
    Unified Client for Local Inference (Ollama).
    """
    
    def __init__(self, base_url: str = "http://localhost:11434", model: str = "llama3"):
        self.base_url = base_url
        self.model = model
        self.is_connected = False
        
    async def check_connection(self) -> bool:
        try:
            async with aiohttp.ClientSession() as session:
                async with session.get(f"{self.base_url}/api/tags", timeout=2) as resp:
                     if resp.status == 200:
                         self.is_connected = True
                         return True
        except:
             self.is_connected = False
        return False
        
    async def chat(self, messages: List[Dict[str, str]], stream: bool = False) -> Dict:
        """
        Chat completion.
        """
        if not self.is_connected:
             # Fast fail check if we haven't checked recently? 
             # For now, try connect, if fail, mock.
             connected = await self.check_connection()
             if not connected:
                 return await self.mock_chat(messages)

        url = f"{self.base_url}/api/chat"
        payload = {
            "model": self.model,
            "messages": messages,
            "stream": stream
        }
        
        try:
            async with aiohttp.ClientSession() as session:
                async with session.post(url, json=payload, timeout=60) as response:
                    if response.status != 200:
                        return {"error": f"Ollama Error {response.status}"}
                    
                    if stream:
    async def chat_stream(self, messages: List[Dict[str, str]]) -> AsyncGenerator[str, None]:
        """
        Stream tokens from Ollama.
        """
        if not self.is_connected:
             if not await self.check_connection():
                 # Mock Stream
                 yield "I am in Mock Mode (Ollama not found). "
                 yield "I cannot stream real tokens yet."
                 return

        url = f"{self.base_url}/api/chat"
        payload = {
            "model": self.model,
            "messages": messages,
            "stream": True
        }
        
        try:
            async with aiohttp.ClientSession() as session:
                async with session.post(url, json=payload, timeout=300) as response:
                    async for line in response.content:
                        if line:
                            try:
                                chunk = json.loads(line)
                                if "message" in chunk and "content" in chunk["message"]:
                                    yield chunk["message"]["content"]
                            except:
                                pass
        except Exception as e:
            logger.error(f"Stream Error: {e}")
            yield f"[Error: {str(e)}]"
                        
        except Exception as e:
            logger.error(f"LLM Error: {e}")
            return await self.mock_chat(messages)

    async def mock_chat(self, messages: List[Dict[str, str]]) -> Dict:
        """Mock fallback"""
        last_msg = messages[-1]["content"].lower()
        content = "I'm in Mock Mode (Ollama not detected). I can simulate tool usage."
        tool_call = None
        
        if "face" in last_msg and "restore" in last_msg:
            content = "I'll fix the faces for you."
            tool_call = {"tool": "restore_face", "params": {}}
        elif "remove" in last_msg and "bg" in last_msg:
             content = "Removing background now."
             tool_call = {"tool": "remove_background", "params": {}}
        elif "generate" in last_msg:
             content = "Generating that image."
             tool_call = {"tool": "generate", "params": {"prompt": last_msg}}
             
        # Simulate LLM JSON output for tool
        if tool_call:
             content = json.dumps(tool_call)
             
        # Artificial delay
        await asyncio.sleep(0.5)
             
        return {
            "message": {
                "role": "assistant",
                "content": content
            }
        }
