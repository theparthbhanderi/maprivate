import logging
import json
import uuid
from typing import List, Dict, Any, Optional

from app.engine.assistant.llm_client import LLMClient
from app.engine.assistant.memory_store import memory_store
from app.engine.assistant.prompt_manager import prompt_manager
from app.engine.assistant.tool_router import tool_router
from app.engine.assistant.safety_filter import SafetyFilter

from app.engine.assistant.rate_limiter import rate_limiter

logger = logging.getLogger(__name__)

class ChatService:
    """
    Orchestrator for AI Assistant.
    """
    
    def __init__(self):
        self.llm = LLMClient()
        
    async def chat_stream_generator(self, message: str, session_id: str) -> AsyncGenerator[str, None]:
        if not message: return
        
        # 0. Rate Limit
        if not rate_limiter.check(session_id):
            yield "⚠️ You are sending messages too fast. Please wait a moment."
            return

        # 1. Update Memory (User)
        memory_store.add_message(session_id, "user", message)
        history = memory_store.get_history(session_id)
        
        # 2. Format
        formatted_messages = prompt_manager.format_history(history)
        
        # 3. Stream
        full_response = ""
        async for token in self.llm.chat_stream(formatted_messages):
            full_response += token
            yield token
            
        # 4. Post-Process (Tool Detection not supported well in stream-mode yet, simple text only)
        # We save full response to memory
        memory_store.add_message(session_id, "assistant", full_response)
        """
        Unified Chat Interface.
        """
        if not session_id:
            session_id = str(uuid.uuid4())
            
        # 1. Safety Filter (Input)
        if not SafetyFilter.check(message):
            return {
                "role": "assistant",
                "content": "I cannot respond to that request due to safety guidelines.",
                "session_id": session_id
            }
            
        # 2. Update Memory
        memory_store.add_message(session_id, "user", message)
        history = memory_store.get_history(session_id)
        
        # 3. Format Prompt (Inject System Prompt)
        formatted_messages = prompt_manager.format_history(history)
        
        # 4. Generate with LLM
        response = await self.llm.chat(formatted_messages)
        
        if response.get("error"):
             return {
                 "role": "assistant",
                 "content": "Sorry, I'm having trouble connecting to my brain (Ollama).",
                 "session_id": session_id
             }
             
        ai_content = response.get("message", {}).get("content", "")
        
        # 5. Tool Detection
        tool_call = None
        # Try to find JSON in content. LLM might wrap in markdown ```json ... ``` or just text
        clean_content = ai_content.strip()
        
        # Naive extraction of first valid JSON object
        try:
             start_idx = clean_content.find("{")
             end_idx = clean_content.rfind("}")
             if start_idx != -1 and end_idx != -1:
                 json_str = clean_content[start_idx:end_idx+1]
                 data = json.loads(json_str)
                 if "tool" in data:
                     if tool_router.validate_call(data["tool"], data.get("params", {})):
                         tool_call = data
                         # Improve the text response to look natural if it was just JSON
                         if len(clean_content) < len(json_str) + 10:
                             ai_content = f"I'm activating {data['tool']} for you."
        except Exception as e:
            # Not a valid tool call, treat as text
            pass
            
        # 6. Safety Filter (Output)
        ai_content = SafetyFilter.sanitize_output(ai_content)
        
        # 7. Update Memory with Response
        memory_store.add_message(session_id, "assistant", ai_content)
        
        return {
            "role": "assistant",
            "content": ai_content,
            "tool_call": tool_call,
            "session_id": session_id
        }

chat_service = ChatService()
