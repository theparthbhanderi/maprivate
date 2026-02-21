import logging
import numpy as np
import cv2
from app.engine.loader import model_manager

logger = logging.getLogger(__name__)

class GenerativeController:
    """
    Controller for Generative AI tasks.
    """
    
    def generate(self, 
                 prompt: str, 
                 image: np.ndarray = None, 
                 mask: np.ndarray = None, 
                 control_type: str = None, 
                 **kwargs) -> np.ndarray:
        """
        Run generation.
        """
        # 1. Get Model
        model = model_manager.get_model("generative_sd")
        
        # 2. Preprocess Control Image (if needed)
        # e.g. if control_type='canny', we might need to edge-detect the input 'image'
        # unless 'image' is already the map. 
        # For simplicity, we assume 'image' is the init image or control image.
        
        # 3. Run
        result = model.generate(
            prompt=prompt,
            image=image,
            mask=mask,
            control_type=control_type,
            **kwargs
        )
        
        return result

generative_controller = GenerativeController()
