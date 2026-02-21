import cv2
import numpy as np
import logging
import json
from app.engine.segmentation.base import SegmentationModel
from app.engine.segmentation.factory import SegmentationFactory
from app.engine.utils.normalizer import ImageNormalizer

logger = logging.getLogger(__name__)

class SAMRunner(SegmentationModel):
    def load(self) -> None:
        logger.info("Loading Segment Anything Model (SAM)...")
        self.model = "MOCK_SAM"
        self.is_loaded = True

    def predict(self, img: np.ndarray, points=None, labels=None, prompts=None, **kwargs) -> np.ndarray:
        if not self.is_loaded:
            self.load()
            
        logger.info(f"Running SAM Guidance: Points={points}, Prompts={prompts}")
        
        # Parse points
        active_points = []
        if points:
            if isinstance(points, str):
                try: active_points = json.loads(points)
                except: logger.warning("Failed to parse SAM points JSON")
            elif isinstance(points, list):
                active_points = points

        if prompts and not active_points:
             if isinstance(prompts, str):
                try:
                    p_data = json.loads(prompts)
                    if isinstance(p_data, list): active_points = p_data
                    elif isinstance(p_data, dict) and 'points' in p_data: active_points.extend(p_data['points'])
                except: pass
        
        # Optimization: ROI Tiling (Smart Crop)
        # If we have points, we can crop around them to save VRAM and increase detail
        if active_points:
            # Calculate Crop Box
            crop_box, rel_points = ImageNormalizer.get_crop_around_points(img.shape, active_points, padding=200, min_size=512)
            x, y, w, h = crop_box
            
            logger.info(f"SAM Smart Crop: {w}x{h} at ({x},{y})")
            
            # Crop
            crop_img = img[y:y+h, x:x+w]
            
            # Run Inference on Crop (simulated/real)
            crop_mask = self._run_inference_on_patch(crop_img, rel_points)
            
            # Create full mask
            full_mask = np.zeros(img.shape[:2], dtype=np.uint8)
            full_mask[y:y+h, x:x+w] = crop_mask
            
            # Merge
            b, g, r = cv2.split(img)
            return cv2.merge([b, g, r, full_mask])
            
        else:
            # Fallback (Auto Center)
            h, w = img.shape[:2]
            mask = np.zeros((h, w), dtype=np.uint8)
            cv2.circle(mask, (w//2, h//2), min(w,h)//3, 255, -1)
            b, g, r = cv2.split(img)
            return cv2.merge([b, g, r, mask])

    def _run_inference_on_patch(self, img: np.ndarray, points: list) -> np.ndarray:
        """
        Run SAM on a specific image patch.
        Returns: Binary Mask (H, W)
        """
        h, w = img.shape[:2]
        mask = np.zeros((h, w), dtype=np.uint8)
        
        # Mock Logic: Draw circles (simulating detection)
        for pt in points:
             # pt is relative to crop
             if len(pt) >= 2:
                cx, cy = int(pt[0]), int(pt[1])
                cv2.circle(mask, (cx, cy), 150, 255, -1)
                
        # Refine
        mask = cv2.GaussianBlur(mask, (21, 21), 0)
        return mask

SegmentationFactory.register("sam", SAMRunner)
