import cv2
import numpy as np
import logging

logger = logging.getLogger(__name__)

class MaskRefiner:
    """
    Utilities for refining binary masks (feathering, smoothing, hair details).
    """

    @staticmethod
    def refine_mask(mask: np.ndarray, feather: int = 0) -> np.ndarray:
        """
        Refine a binary/alpha mask.
        mask: Single channel (H, W).
        feather: Amount of gaussian blur to apply to edges.
        """
        refined = mask.copy()
        
        # 1. Clean small noise (Morphological Opening)
        # Only if mask is binary-ish. If it's soft alpha, skip.
        if np.max(refined) > 0 and feather == 0:
            kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (3, 3))
            refined = cv2.morphologyEx(refined, cv2.MORPH_OPEN, kernel, iterations=1)
        
        # 2. Feather Edges
        if feather > 0:
            # Ensure feather size is odd
            ksize = feather if feather % 2 == 1 else feather + 1
            refined = cv2.GaussianBlur(refined, (ksize, ksize), 0)
            
        return refined

    @staticmethod
    def smooth_edges(mask: np.ndarray, strength: int = 1) -> np.ndarray:
        """
        Apply Guided Filter or simple blur to smooth jagged edges from low-res masks.
        """
        if strength <= 0: return mask
        
        # Simple approach: Blur then threshold?
        # Better: Guided Filter if we had the original image, but here we just have mask.
        # Fallback to simple blur.
        blurred = cv2.GaussianBlur(mask, (3, 3), 0)
        return blurred

    @staticmethod
    def apply_mask(image: np.ndarray, mask: np.ndarray) -> np.ndarray:
        """
        Apply alpha mask to image. Returns RGBA.
        """
        if len(mask.shape) == 3:
            mask = cv2.cvtColor(mask, cv2.COLOR_BGR2GRAY)
            
        # Ensure mask same size
        if mask.shape[:2] != image.shape[:2]:
            mask = cv2.resize(mask, (image.shape[1], image.shape[0]))
            
        b, g, r = cv2.split(image)
        return cv2.merge([b, g, r, mask])
