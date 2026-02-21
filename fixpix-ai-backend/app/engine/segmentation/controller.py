import logging
import numpy as np
from app.engine.loader import model_manager
from app.engine.segmentation.utils.mask_refiner import MaskRefiner

logger = logging.getLogger(__name__)

class SegmentationController:
    """
    Orchestrates segmentation tasks.
    Routes to RMBG (Auto) or SAM (Interactive).
    """
    
    def segment(self, image: np.ndarray, mode: str = "auto", points=None, labels=None, prompts=None, feather: int = 0) -> np.ndarray:
        """
        Unified segmentation interface.
        
        Args:
            image: Input image (BGR)
            mode: 'auto' | 'interactive' | 'refine'
            points: List of [x, y] for SAM
            labels: List of [1, 0] labels for SAM
            prompts: Generic prompts (box, points)
            feather: Refine edges (0-40)
            
        Returns:
            RGBA Image
        """
        logger.info(f"Segmentation Controller: Mode={mode}")
        
        try:
            if mode == "auto":
                # RMBG-1.4
                model = model_manager.get_model("segmentation_rmbg")
                rgba = model.predict(image)
                
            elif mode in ["interactive", "refine"]:
                # SAM
                model = model_manager.get_model("segmentation_sam")
                rgba = model.predict(image, points=points, labels=labels, prompts=prompts)
                
            else:
                raise ValueError(f"Unknown segmentation mode: {mode}")

            # Extract generated mask
            _, _, _, mask = cv2.split(rgba)
            
            # Refine Mask
            if feather > 0:
                mask = MaskRefiner.refine_mask(mask, feather=feather)
                
            # Re-merge
            b, g, r, _ = cv2.split(rgba)
            return cv2.merge([b, g, r, mask])
            
        except Exception as e:
            logger.error(f"Segmentation Controller failed: {e}")
            raise e

segmentation_controller = SegmentationController()
