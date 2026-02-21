from abc import ABC, abstractmethod
import numpy as np
from app.engine.base import AIModel

class UpscalerModel(AIModel):
    """
    Abstract base class for Super-Resolution models.
    """
    
    @abstractmethod
    def predict(self, img: np.ndarray, scale: int = 4, **kwargs) -> np.ndarray:
        """
        Upscale the image by the given factor.
        """
        pass
