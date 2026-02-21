import cv2
import numpy as np
import logging

logger = logging.getLogger(__name__)

class ImagePostprocessor:
    """
    Utilities for enhancing AI outputs (Post-Processing).
    """
    
    @staticmethod
    def auto_contrast(image: np.ndarray, clip_limit: float = 2.0) -> np.ndarray:
        """
        Apply CLAHE (Contrast Limited Adaptive Histogram Equalization) to L-channel.
        """
        try:
            # Convert to LAB
            lab = cv2.cvtColor(image, cv2.COLOR_BGR2LAB)
            l, a, b = cv2.split(lab)
            
            # Apply CLAHE to L-channel
            clahe = cv2.createCLAHE(clipLimit=clip_limit, tileGridSize=(8, 8))
            cl = clahe.apply(l)
            
            # Merge
            limg = cv2.merge((cl, a, b))
            
            # Convert back to BGR
            return cv2.cvtColor(limg, cv2.COLOR_LAB2BGR)
        except Exception as e:
            logger.error(f"Auto contrast failed: {e}")
            return image

    @staticmethod
    def boost_saturation(image: np.ndarray, factor: float = 1.3) -> np.ndarray:
        """
        Boost saturation of the image.
        """
        try:
            hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV).astype("float32")
            (h, s, v) = cv2.split(hsv)
            
            s = s * factor
            s = np.clip(s, 0, 255)
            
            hsv = cv2.merge([h, s, v])
            return cv2.cvtColor(hsv.astype("uint8"), cv2.COLOR_HSV2BGR)
        except Exception as e:
            logger.error(f"Saturation boost failed: {e}")
            return image
            
    @staticmethod
    def sharpen(image: np.ndarray) -> np.ndarray:
        """
        Slight sharpening to crisp up details.
        """
        kernel = np.array([[0, -1, 0],
                           [-1, 5,-1],
                           [0, -1, 0]])
        return cv2.filter2D(image, -1, kernel)

    @staticmethod
    def enhance(image: np.ndarray, contrast: bool = True, saturation: bool = True) -> np.ndarray:
        """
        Pipeline for DeOldify results which are often washed out.
        """
        res = image
        if contrast:
            res = ImagePostprocessor.auto_contrast(res, clip_limit=1.2)
        if saturation:
            res = ImagePostprocessor.boost_saturation(res, factor=1.2)
        return res
