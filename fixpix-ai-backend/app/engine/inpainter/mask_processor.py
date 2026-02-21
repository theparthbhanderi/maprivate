import cv2
import numpy as np
import logging

logger = logging.getLogger(__name__)

class MaskProcessor:
    @staticmethod
    def process(mask: np.ndarray, dilate_iter: int = 5, blur_radius: int = 9) -> np.ndarray:
        """
        Prepare binary mask for inpainting.
        1. Threshold
        2. Dilate (expand slightly to cover edges)
        3. Soften edges (Blur)
        """
        # Ensure 2D
        if mask.ndim == 3:
            mask = cv2.cvtColor(mask, cv2.COLOR_BGR2GRAY)
            
        # Binary Threshold
        _, mask = cv2.threshold(mask, 127, 255, cv2.THRESH_BINARY)
        
        # Dilate to ensure we cover the object fully
        kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5))
        mask = cv2.dilate(mask, kernel, iterations=dilate_iter)
        
        # Soften edges for better blending options (though LaMa usually likes sharp masks, SD likes soft)
        # We'll return a dilated mask, maybe no blur for LaMa, simpler is better.
        # Actually, for LaMa, binary mask is standard. 
        # For SD, sometimes a slightly soft mask helps blending.
        # Let's just dilate.
        
        return mask

    @staticmethod
    def is_empty(mask: np.ndarray) -> bool:
        """
        Check if mask has any white pixels.
        """
        # Ensure 2D
        if mask.ndim == 3:
            mask = cv2.cvtColor(mask, cv2.COLOR_BGR2GRAY)
        return cv2.countNonZero(mask) == 0
