import time
import logging
from typing import Dict

logger = logging.getLogger(__name__)

class RateLimiter:
    """
    Token Bucket Rate Limiter per Session.
    """
    
    def __init__(self, rate: int = 10, per: int = 60):
        self.rate = rate # Tokens per period
        self.per = per   # Period in seconds
        self.tokens: Dict[str, float] = {}
        self.last_update: Dict[str, float] = {}
        
    def check(self, session_id: str) -> bool:
        now = time.time()
        
        # Initialize if new
        if session_id not in self.tokens:
            self.tokens[session_id] = self.rate
            self.last_update[session_id] = now
            return True
            
        # Refill tokens based on time passed
        elapsed = now - self.last_update[session_id]
        refill = elapsed * (self.rate / self.per)
        self.tokens[session_id] = min(self.rate, self.tokens[session_id] + refill)
        self.last_update[session_id] = now
        
        # Consume token
        if self.tokens[session_id] >= 1:
            self.tokens[session_id] -= 1
            return True
        else:
            logger.warning(f"Rate Limit Exceeded for {session_id}")
            return False

rate_limiter = RateLimiter(rate=10, per=60) # 10 requests per minute
