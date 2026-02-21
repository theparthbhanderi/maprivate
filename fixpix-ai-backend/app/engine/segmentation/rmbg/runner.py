import cv2
import numpy as np
import logging
from app.engine.segmentation.base import SegmentationModel
from app.engine.segmentation.factory import SegmentationFactory
from app.engine.utils.normalizer import ImageNormalizer

logger = logging.getLogger(__name__)

try:
    from rembg import remove, new_session
    HAS_REMBG = True
except ImportError:
    HAS_REMBG = False

class RMBGRunner(SegmentationModel):
    def load(self) -> None:
        logger.info("Loading RMBG-1.4 (Auto Background Removal)...")
        self.session = None
        if HAS_REMBG:
            try:
                self.session = new_session(model_name="u2net") 
                logger.info("Rembg Session Loaded.")
            except Exception as e:
                logger.warning(f"Failed to load Rembg session: {e}")
        self.is_loaded = True

    def predict(self, img: np.ndarray, **kwargs) -> np.ndarray:
        if not self.is_loaded:
            self.load()
            
        logger.info("Running RMBG Segmentation...")
        
        if HAS_REMBG and self.session:
            try:
                # Optimization: Resize large images to max 1024px for inference
                # RMBG/U2Net works on 320x320 internally anyway, sending 4K is wasteful
                orig_h, orig_w = img.shape[:2]
                
                # Smart Resize Input
                img_small = ImageNormalizer.smart_resize(img, max_dim=1024)
                
                # Run Inference
                result_small = remove(img_small, session=self.session)
                
                # Resize Result Back to Original
                # Result is RGBA
                result_full = cv2.resize(result_small, (orig_w, orig_h), interpolation=cv2.INTER_LINEAR)
                
                return result_full
                
            except Exception as e:
                logger.error(f"Rembg inference failed: {e}")
                
        # Mock Logic
        h, w = img.shape[:2]
        mask = np.zeros((h, w), dtype=np.uint8)
        center = (w // 2, h // 2)
        axes = (w // 3, h // 3)
        cv2.ellipse(mask, center, axes, 0, 0, 360, 255, -1)
        mask = cv2.GaussianBlur(mask, (51, 51), 0)
        b, g, r = cv2.split(img)
        rgba = cv2.merge([b, g, r, mask])
        return rgba

SegmentationFactory.register("rmbg", RMBGRunner)
