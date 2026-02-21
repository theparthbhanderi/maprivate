from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class ColorizerModel(AIModel):
    """
    Abstract base class for Colorization models (DeOldify).
    """
    
    @abstractmethod
    def predict(self, img: np.ndarray, render_factor: int = 35, **kwargs) -> np.ndarray:
        """
        Colorize the B&W image.
        
        Args:
            img: Input image (H, W, 3) BGR or RGB (Grayscale converted to 3-channel)
            render_factor: Resolution parameter (10-45). Higher = more detail but slower/more stable.
            
        Returns:
            Colorized image (BGR)
        """
        pass
