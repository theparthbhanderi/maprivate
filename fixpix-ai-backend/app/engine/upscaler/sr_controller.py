from app.engine.upscaler.factory import UpscalerFactory
from app.engine.loader import model_manager
from app.core.limits import memory_watchdog
import logging
import numpy as np

logger = logging.getLogger(__name__)

class SRController:
    """
    Super-Resolution Controller.
    Manages the upscaling pipeline with Caching and Optimization.
    """
    def __init__(self):
        self.cache = {}
        self.cache_order = []
        self.MAX_CACHE = 20
        # Determine tile size based on system
        import torch
        if torch.cuda.is_available():
             # Basic VRAM heuristic
             # Real implementation would use pynvml, but let's assume T4 typical behavior
             self.default_tile_size = 512
             logger.info("CUDA detected: Using 512px tiles")
        else:
             self.default_tile_size = 256 # Safe for CPU/MPS
             logger.info("CPU/MPS detected: Using 256px tiles")

    def _get_cache_key(self, img, scale, enhance_faces, fast_mode):
        # Hash image content + params
        # Simple sampling hash for speed (not cryptographically secure but prevents full scan)
        img_hash = hash(img.tobytes()[::100]) 
        return f"{img_hash}_{scale}_{enhance_faces}_{fast_mode}"

    def upscale(self, img: np.ndarray, scale: int = 4, enhance_faces: bool = False, fast_mode: bool = False) -> np.ndarray:
        """
        Main Upscale Entry Point.
        """
        # 0. Cache Check
        cache_key = self._get_cache_key(img, scale, enhance_faces, fast_mode)
        if cache_key in self.cache:
            logger.info("Cache Hit! Returning cached result.")
            # Move to end (LRU)
            self.cache_order.remove(cache_key)
            self.cache_order.append(cache_key)
            return self.cache[cache_key]

        # 1. Resource Check
        if not memory_watchdog.check_resources():
            logger.warning("Low RAM/VRAM. Triggering rigorous GC before Upscale.")
            import gc
            import torch
            gc.collect()
            if torch.cuda.is_available():
                torch.cuda.empty_cache()
                
        # 2. Load Upscaler
        model = model_manager.get_model("upscaler")
        
        # Configure Tiling if applicable to the runner (monkey patch or param if supported)
        # Our RealESRGANRunner creates its own tile manager. 
        # Ideally we pass it. For now, we trust its defaults (400) or our overrides.
        if hasattr(model, 'tile_manager'):
            model.tile_manager.tile_size = self.default_tile_size
        
        # 3. Predict (Super Resolution)
        try:
            result = model.predict(img, scale=scale)
        except Exception as e:
            logger.error(f"Upscaling failed: {e}")
            raise e
            
        # 4. Optional Face Enhancement (Post-Upscale)
        if enhance_faces:
            logger.info("Applying Face Enhancement after Upscale...")
            from app.engine.controller import hybrid_controller
            mode = "fast" if fast_mode else "quality"
            enhanced_img, _ = hybrid_controller.restore(result, mode=mode)
            result = enhanced_img
            
        # 5. Cache Result
        if len(self.cache) >= self.MAX_CACHE:
            oldest = self.cache_order.pop(0)
            del self.cache[oldest]
        self.cache[cache_key] = result
        self.cache_order.append(cache_key)
        
        return result

sr_controller = SRController()
