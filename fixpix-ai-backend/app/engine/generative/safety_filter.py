import logging
from typing import List

logger = logging.getLogger(__name__)

class SafetyFilter:
    """
    Simple safety filter for prompts and images.
    """
    
    NSFW_KEYWORDS = [
        "nude", "naked", "sex", "porn", "xxx", "nsfw" 
        # Add more as needed for rigorous filtering
    ]
    
    @staticmethod
    def check_prompt(prompt: str) -> bool:
        """
        Check if prompt matches banned keywords.
        Returns True if SAFE, False if UNSAFE.
        """
        if not prompt: return True
        
        lower_p = prompt.lower()
        for word in SafetyFilter.NSFW_KEYWORDS:
            if word in lower_p: # Simple substring check (can be naive but effective for MVP)
                logger.warning(f"SafetyFilter caught: {word}")
                return False
                
        return True

    @staticmethod
    def check_image(image) -> bool:
        """
        Check generated image (requires actual safety checker model).
        Placeholder returns True (Safe).
        """
        # In real diffusers pipeline, 'safety_checker' arg handles this.
        # This is for manual post-check if needed.
        return True
