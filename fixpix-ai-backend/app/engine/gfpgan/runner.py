import os
import torch
import numpy as np
from app.engine.base import AIModel
from app.core.config import settings
import logging

try:
    from gfpgan import GFPGANer
except ImportError:
    GFPGANer = None

logger = logging.getLogger(__name__)

class GFPGANRunner(AIModel):
    def load(self) -> None:
        if GFPGANer is None:
            logger.warning("GFPGAN library not installed, run pip install gfpgan")
            self.model = "MOCK"
            self.is_loaded = True
            return

        logger.info("Loading GFPGAN model...")
        
        # Ensure weights exist or download them
        from app.core.drive_utils import ensure_model
        model_path = ensure_model("gfpgan", "GFPGANv1.4.pth")
        
        # Fallback if download skipped/failed but path constructed
        if not model_path:
             model_path = os.path.join(settings.MODEL_CACHE_DIR, 'GFPGANv1.4.pth')
        
        self.model = GFPGANer(
            model_path=model_path,
            upscale=2,
            arch='clean',
            channel_multiplier=2,
            bg_upsampler=None, # We handle BG separately if needed, or set 'realesrgan'
            device=self.device
        )
        self.is_loaded = True
        logger.info("GFPGAN model loaded.")

    def predict(self, img: np.ndarray) -> np.ndarray:
        if not self.is_loaded:
            self.load()
            
        if self.model == "MOCK":
            return img

        # enhance(img, has_aligned=False, only_center_face=False, paste_back=True)
        # weight=0.5 is default fusion
        _, _, output = self.model.enhance(img, has_aligned=False, only_center_face=False, paste_back=True, weight=0.5)
        return output
