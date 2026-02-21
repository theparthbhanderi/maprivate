import logging
from typing import List, Dict, Optional
import time
from collections import deque

logger = logging.getLogger(__name__)

class MemoryStore:
    """
    In-memory storage for chat sessions.
    Production should use Redis.
    """
    def __init__(self, max_history: int = 20):
        self.sessions: Dict[str, deque] = {}
        self.max_history = max_history
        self.last_access: Dict[str, float] = {}
        
    def get_history(self, session_id: str) -> List[Dict[str, str]]:
        if session_id not in self.sessions:
            return []
        self.last_access[session_id] = time.time()
        return list(self.sessions[session_id])
        
    def add_message(self, session_id: str, role: str, content: str):
        if session_id not in self.sessions:
            self.sessions[session_id] = deque(maxlen=self.max_history)
            
        self.sessions[session_id].append({"role": role, "content": content})
        self.last_access[session_id] = time.time()
        
    def clear_session(self, session_id: str):
        if session_id in self.sessions:
            del self.sessions[session_id]
        if session_id in self.last_access:
            del self.last_access[session_id]
            
    def cleanup_old_sessions(self, timeout_seconds: int = 3600):
        """Remove sessions inactive for > timeout"""
        now = time.time()
        to_del = []
        for sid, ts in self.last_access.items():
            if now - ts > timeout_seconds:
                to_del.append(sid)
        
        for sid in to_del:
            self.clear_session(sid)
            
memory_store = MemoryStore()
