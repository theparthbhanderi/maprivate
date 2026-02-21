import os
import cv2
import numpy as np
import logging
import torch
import gc
from app.engine.colorizer.base import ColorizerModel
from app.engine.colorizer.factory import ColorizerFactory
from app.engine.utils.postprocessor import ImagePostprocessor
from app.engine.utils.normalizer import ImageNormalizer
from app.core.config import settings

logger = logging.getLogger(__name__)

# Try importing DeOldify wrapper logic
try:
    from deoldify.visualize import get_image_colorizer
    HAS_DEOLDIFY = True
except ImportError:
    HAS_DEOLDIFY = False

class DeOldifyRunner(ColorizerModel):
    def __init__(self, device):
        super().__init__(device)
        self.mode = "artistic" # Default
        self.colorizer = None
        
    def load(self, mode: str = "artistic") -> None:
        """
        Load DeOldify model.
        mode: 'artistic' or 'stable'
        """
        logger.info(f"Loading DeOldify ({mode})...")
        
        if not HAS_DEOLDIFY:
            self.model = "MOCK_DEOLDIFY"
            self.is_loaded = True
            return
            
        try:
            if self.device.type == 'cpu':
                torch.device('cpu') 
            else:
                torch.device('cuda')
                
            self.colorizer = get_image_colorizer(artistic=(mode == "artistic"))
            self.mode = mode
            self.is_loaded = True
            logger.info("DeOldify Loaded Successfully.")
            
        except Exception as e:
            logger.error(f"Failed to load DeOldify: {e}")
            self.model = "MOCK_DEOLDIFY"
            self.is_loaded = True

    def predict(self, img: np.ndarray, mode: str = "artistic", render_factor: int = 35, **kwargs) -> np.ndarray:
        if not self.is_loaded or (HAS_DEOLDIFY and self.colorizer and self.mode != mode):
            self.load(mode=mode)
            
        logger.info(f"Running DeOldify: Mode={mode}, Factor={render_factor}")
        
        # 0. Mock Fallback
        if getattr(self, "model", None) == "MOCK_DEOLDIFY":
            return self._mock_colorize(img, mode)

        if not self.colorizer:
             raise RuntimeError("DeOldify not initialized")
        
        # 1. Image Optimization (Smart Resize)
        # Cap max dimension to 2048px to prevent huge OOM on consumer GPUs.
        # DeOldify doesn't benefit much from >2k res as it's coloring features.
        img_work = ImageNormalizer.smart_resize(img, max_dim=2048)
        
        # 2. Inference with Retry Logic (OOM Protection)
        result = self._run_inference_with_retry(img_work, render_factor)
        
        # 3. Post Process
        # Auto Contrast/Sat
        result = ImagePostprocessor.enhance(result, contrast=True, saturation=(mode=="artistic"))
        
        # Optional: Face Enhancement
        if kwargs.get('enhance_faces', False):
            logger.info("Applying Face Enhancement (GFPGAN)...")
            try:
                from app.engine.loader import model_manager
                gfpgan = model_manager.get_model("gfpgan") 
                
                # Check if result is huge, maybe resize slightly for GFPGAN to be safe
                # GFPGAN is robust but slow on massive images. 
                # Our smart_resize already caps at 2048, so we are safe.
                
                result = gfpgan.predict(result)
            except Exception as e:
                logger.warning(f"Face Enhancement failed (skipping): {e}")

        return result
    
    def _run_inference_with_retry(self, img: np.ndarray, render_factor: int) -> np.ndarray:
        """
        Attempt inference. If OOM, reduce render_factor and retry.
        """
        factors_to_try = [render_factor]
        if render_factor > 25:
            factors_to_try.append(20)
        if render_factor > 15:
            factors_to_try.append(10)
            
        from PIL import Image
        import tempfile
        pil_img = Image.fromarray(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
        
        last_error = None
        
        for factor in factors_to_try:
            try:
                # Clean memory before run
                if self.device.type == 'cuda':
                    torch.cuda.empty_cache()
                
                # Run Logic
                result_pil = self.colorizer.get_transformed_image(pil_img, render_factor=factor) \
                             if hasattr(self.colorizer, 'get_transformed_image') else None
                
                if not result_pil:
                     # File based fallback
                     with tempfile.NamedTemporaryFile(suffix=".jpg", delete=False) as tmp:
                         pil_img.save(tmp.name)
                         tmp_path = tmp.name
                     try:    
                         result_pil = self.colorizer.get_transformed_image(tmp_path, render_factor=factor)
                     finally:
                         if os.path.exists(tmp_path):
                            os.remove(tmp_path)

                logger.info(f"DeOldify Success (Factor={factor})")
                return cv2.cvtColor(np.array(result_pil), cv2.COLOR_RGB2BGR)
                
            except RuntimeError as e:
                if "out of memory" in str(e).lower():
                    logger.warning(f"OOM with factor {factor}. Retrying with lower factor...")
                    if self.device.type == 'cuda':
                        torch.cuda.empty_cache()
                    last_error = e
                    continue
                else:
                    raise e
            except Exception as e:
                logger.error(f"Inference failed with factor {factor}: {e}")
                last_error = e
                # Don't strictly retry on generic errors unless we want to catch all
                continue
                
        # If we got here, all attempts failed.
        logger.error("All DeOldify attempts failed.")
        if last_error:
            raise last_error
        return img # Should not happen

    def _mock_colorize(self, img: np.ndarray, mode: str) -> np.ndarray:
        logger.warning(f"Using Mock Colorization ({mode})")
        if len(img.shape) == 3:
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        else:
            gray = img
            
        color = cv2.cvtColor(gray, cv2.COLOR_GRAY2BGR)
        b, g, r = cv2.split(color)
        
        if mode == "artistic":
            # Warm Sepia
            r = np.clip(r * 1.3, 0, 255).astype(np.uint8)
            g = np.clip(g * 1.1, 0, 255).astype(np.uint8)
            b = np.clip(b * 0.9, 0, 255).astype(np.uint8)
        else:
            # Cooler/Stable
            b = np.clip(b * 1.1, 0, 255).astype(np.uint8)
        
        return cv2.merge([b, g, r])

ColorizerFactory.register("deoldify", DeOldifyRunner)
