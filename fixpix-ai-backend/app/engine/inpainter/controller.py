from app.engine.inpainter.factory import InpainterFactory
from app.engine.loader import model_manager
from app.core.limits import memory_watchdog
import logging
import numpy as np

logger = logging.getLogger(__name__)

class InpaintController:
    """
    Unified Controller for Inpainting Tasks.
    """
    
    def inpaint(self, 
                image: np.ndarray, 
                mask: np.ndarray, 
                mode: str = "fast", 
                prompt: str = None, 
                strength: float = 0.8,
                feathering: int = 9) -> np.ndarray:
        """
        Unified Interface.
        mode: "fast" (LaMa) | "smart" (Stable Diffusion)
        """
        # 1. Resource Check
        if not memory_watchdog.check_resources():
             # Force fast mode if VRAM low? 
             import gc
             gc.collect()

        # 2. Empty Check (Fail fast)
        from app.engine.inpainter.mask_processor import MaskProcessor
        if MaskProcessor.is_empty(mask):
            return image

        # 3. Helper for prediction
        def run_prediction(m_name):
            mdl = model_manager.get_model(m_name)
            return mdl.predict(image, mask, prompt=prompt, strength=strength, feathering=feathering)

        # 4. Strategy Execution
        try:
            target_model = "inpainter_lama"
            if mode == "smart":
                target_model = "inpainter_sd"
            
            return run_prediction(target_model)
            
        except Exception as e:
            logger.error(f"Inpainting ({mode}) failed: {e}")
            
            # Fallback Logic
            if mode == "smart":
                logger.warning("Falling back to LaMa (Fast Mode)...")
                try:
                    return run_prediction("inpainter_lama")
                except Exception as e2:
                    logger.error(f"Fallback also failed: {e2}")
                    raise e
            raise e

inpaint_controller = InpaintController()
