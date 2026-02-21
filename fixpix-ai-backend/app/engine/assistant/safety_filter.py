import logging
import re

logger = logging.getLogger(__name__)

class SafetyFilter:
    """
    Filters chat inputs and outputs for banned concepts.
    """
    
    BANNED_KEYWORDS = [
        "nsfw", "porn", "nude", "explicit", 
        "violence", "gore", 
        "hate", "racist", "slur"
    ]
    
    @classmethod
    def check(cls, text: str) -> bool:
        """
        Returns True if safe, False if unsafe.
        """
        text_lower = text.lower()
        for word in cls.BANNED_KEYWORDS:
            if word in text_lower:
                logger.warning(f"Safety Filter Triggered on: {word}")
                return False
        return True
    
    @classmethod
    def sanitize_output(cls, text: str) -> str:
        """
        Redacts unsafe content if any slipped through.
        """
        if not cls.check(text):
            return "[Content blocked by safety filter]"
        return text
