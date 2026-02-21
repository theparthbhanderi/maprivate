from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class HumanParsingModel(AIModel):
    """
    Abstract base class for Human Parsing models (SCHP).
    """
    
    @abstractmethod
    def predict(self, img: np.ndarray, **kwargs) -> tuple:
        """
        Parse human image into semantic parts.
        
        Args:
            img: Input image (H, W, 3) BGR or RGB
            
        Returns:
            (parsing_map, palette)
            parsing_map: np.ndarray (H, W) of int class IDs
            palette: Dictionary of {id: "label_name"}
        """
        pass
