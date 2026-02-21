from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class InpainterModel(AIModel):
    """
    Abstract base class for Inpainting models.
    """
    
    @abstractmethod
    def predict(self, img: np.ndarray, mask: np.ndarray, prompt: str = "", **kwargs) -> np.ndarray:
        """
        Inpaint the masked area of the image.
        
        Args:
            img: Input image (H, W, 3) BGR or RGB
            mask: Binary mask (H, W, 1) or (H, W) where >0 is area to inpaint
            prompt: Optional text prompt for generative filling
            
        Returns:
            Inpainted image (same shape as input)
        """
        pass
