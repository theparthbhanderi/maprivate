import numpy as np
import logging
from app.engine.loader import model_manager
from app.engine.detector import FaceDetector
from app.engine.gfpgan.runner import GFPGANRunner
from app.engine.codeformer.runner import CodeFormerRunner
from app.engine.device import get_device

logger = logging.getLogger(__name__)

class HybridFaceRestorer:
    """
    Main entry point for Face Restoration.
    Orchestrates: Detection -> Alignment -> Restoration -> Blending
    """
    def __init__(self):
        self.device = get_device()
        self.detector = FaceDetector(self.device)
        self.gfpgan = GFPGANRunner(self.device)
        self.codeformer = CodeFormerRunner(self.device)

    def restore(self, img: np.ndarray, mode: str = "fast", fidelity: float = 0.5) -> tuple[np.ndarray, int]:
        """
        Main restore method with Fallback Chain.
        mode: 'fast' (GFPGAN) or 'quality' (CodeFormer)
        """
        from app.core.limits import memory_watchdog
        
        logger.info(f"Starting restoration: mode={mode}")
        
        # 1. Resource Check
        if not memory_watchdog.check_resources():
            logger.warning("Insufficient resources! Forcing Fast Mode / Fallback.")
            mode = "fast"
        
        # 2. Hybrid Logic with Fallback
        face_count = 0
        try:
             # Detection Step (Common)
            bboxes = self.detector.predict(img)
            face_count = len(bboxes)
            if face_count == 0:
                logger.info("No faces detected.")
                # We can still run full image enhancement via GFPGAN if desired,
                # or just return original. Let's try GFPGAN as it enhances detail anyway.
        except Exception as e:
            logger.error(f"Detection failed: {e}")
            face_count = 0

        # Try Quality Mode (CodeFormer)
        if mode == "quality":
            try:
                output = self.codeformer.predict(img, fidelity=fidelity)
                return output, face_count
            except RuntimeError as e:
                if "out of memory" in str(e).lower():
                    logger.error("CodeFormer OOM! Falling back to GFPGAN.")
                    self.codeformer.unload() # Cleanup immediately
                else:
                    logger.error(f"CodeFormer failed: {e}")
                
            # If we reach here, CodeFormer failed -> Fallthrough to GFPGAN
            logger.info("Falling back to GFPGAN...")

        # Fast Mode / Fallback (GFPGAN)
        try:
            output = self.gfpgan.predict(img)
            return output, face_count
        except Exception as e:
            logger.error(f"GFPGAN failed: {e}")
            
        # Ultimate Fallback (Return Original)
        logger.warning("All restoration methods failed. Returning original image.")
        return img, face_count

    def cleanup(self):
        """Unload models to free memory"""
        self.detector.unload()
        self.gfpgan.unload()
        self.codeformer.unload()

hybrid_controller = HybridFaceRestorer()
