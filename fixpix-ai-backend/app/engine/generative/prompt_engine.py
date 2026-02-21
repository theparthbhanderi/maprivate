import random

class PromptEngine:
    """
    Utilities for prompt engineering and enhancement.
    """
    
    POSITIVE_PRESETS = [
        "high quality", "8k", "masterpiece", "detailed", "sharp focus", "professional photography"
    ]
    
    NEGATIVE_DEFAULTS = [
        "blurry", "low quality", "lowres", "watermark", "text", "ugly", 
        "deformed", "bad anatomy", "disfigured", "jpeg artifacts"
    ]
    
    @staticmethod
    def enhance_prompt(prompt: str, quality_mode: str = "fast") -> str:
        """
        Enhance prompt with keywords.
        """
        if not prompt: return ""
        
        # Cleanup
        prompt = prompt.strip()
        
        # Don't double add
        lower_prompt = prompt.lower()
        
        extras = []
        if quality_mode == "pro":
            for p in PromptEngine.POSITIVE_PRESETS:
                if p not in lower_prompt:
                    extras.append(p)
                    
        if extras:
            prompt += ", " + ", ".join(extras)
            
        return prompt

    @staticmethod
    def get_negative_prompt(user_negative: str = "") -> str:
        """
        Merge user negative prompt with defaults.
        """
        defaults = set(PromptEngine.NEGATIVE_DEFAULTS)
        
        if user_negative:
            parts = [p.strip() for p in user_negative.split(",")]
            for p in parts:
                defaults.add(p)
                
        return ", ".join(list(defaults))
