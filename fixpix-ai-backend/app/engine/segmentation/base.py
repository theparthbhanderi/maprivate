from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class SegmentationModel(AIModel):
    """
    Abstract base class for Segmentation models (RMBG, SAM).
    """
    
    @abstractmethod
    def predict(self, img: np.ndarray, **kwargs) -> np.ndarray:
        """
        Segment the image.
        
        Args:
            img: Input image (H, W, 3) BGR or RGB
            **kwargs: Extra params like 'points' (SAM) or 'labels'.
            
        Returns:
            Likely an Alpha Mask (H, W) or RGBA Image depending on usage.
            Unified: Return RGBA Image (transparent background).
        """
        pass
