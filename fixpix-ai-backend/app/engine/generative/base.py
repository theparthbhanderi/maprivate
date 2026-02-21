from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class GenerativeModel(AIModel):
    """
    Abstract base class for Generative models (Stable Diffusion, etc.).
    """
    
    @abstractmethod
    def generate(self, prompt: str, image: np.ndarray = None, mask: np.ndarray = None, **kwargs) -> np.ndarray:
        """
        Generate image based on prompt and optional input image/mask.
        
        Args:
            prompt: Text prompt
            image: Optional init image (for I2I or Inpainting or ControlNet)
            mask: Optional mask (for Inpainting)
            **kwargs: Extra params (strength, guidance_scale, seed, control_type)
            
        Returns:
            np.ndarray: Generated image (H, W, 3) RGB
        """
        pass
