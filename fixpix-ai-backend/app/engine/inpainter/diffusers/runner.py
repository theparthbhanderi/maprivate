import torch
import numpy as np
import logging
from PIL import Image
from app.engine.inpainter.base import InpainterModel
from app.engine.inpainter.factory import InpainterFactory
from app.engine.utils.normalizer import ImageNormalizer
from app.engine.inpainter.mask_processor import MaskProcessor

logger = logging.getLogger(__name__)

try:
    from diffusers import StableDiffusionInpaintPipeline
    HAS_DIFFUSERS = True
except ImportError:
    HAS_DIFFUSERS = False

class StableDiffusionRunner(InpainterModel):
    def load(self) -> None:
        logger.info("Loading Stable Diffusion Inpainting...")
        if not HAS_DIFFUSERS:
            self.model = "MOCK_SD"
            self.is_loaded = True
            return

        # Model ID (can be switched to runayml/stable-diffusion-inpainting or local path)
        model_id = "runwayml/stable-diffusion-inpainting"
        
        try:
            dtype = torch.float16 if self.device.type == "cuda" else torch.float32
            self.pipe = StableDiffusionInpaintPipeline.from_pretrained(
                model_id,
                torch_dtype=dtype,
                variant="fp16" if dtype == torch.float16 else None
            )
            self.pipe.to(self.device)
            # Enable memory optimizations
            if self.device.type == "cuda":
                self.pipe.enable_attention_slicing()
                
            self.is_loaded = True
            logger.info("Stable Diffusion loaded.")
        except Exception as e:
            logger.error(f"Failed to load SD: {e}")
            self.model = "MOCK_SD" # Fallback
            self.is_loaded = True

    def predict(self, img: np.ndarray, mask: np.ndarray, prompt: str = "", strength: float = 0.75, feathering: int = 9, **kwargs) -> np.ndarray:
        if not self.is_loaded:
            self.load()
            
        logger.info(f"Running SD Inpainting: '{prompt}' (Strength={strength}, Feather={feathering})")
        
        # Mock Fallback
        if not HAS_DIFFUSERS or getattr(self, "model", None) == "MOCK_SD":
            logger.warning("Running Mock SD (Telea fallback)")
            # Reuse MaskProcessor for consistency
            blur = feathering if feathering % 2 == 1 else feathering + 1
            processed_mask = MaskProcessor.process(mask, blur_radius=blur)
            return cv2.inpaint(img, processed_mask, 3, cv2.INPAINT_TELEA)

        # 1. Validation
        if MaskProcessor.is_empty(mask):
            logger.info("Empty mask detected during SD Inpaint. Returning original.")
            return img

        # 2. Smart Crop optimization
        # Instead of resizing the whole 4K image, crop around the mask.
        padding = 512 # Context size
        logger.info("Applying Smart Crop for SD...")
        
        cropped_img, cropped_mask, bbox = ImageNormalizer.crop_to_mask(img, mask, padding=padding)
        h_crop, w_crop = cropped_img.shape[:2]
        
        # 3. Resize Crop if still too big for SD
        # SD 1.5 runs best at 512-768. 1024 is max reasonable.
        max_sd_dim = 1024
        
        # Check if we need to resize the crop
        needs_resize = max(h_crop, w_crop) > max_sd_dim
        
        if needs_resize:
             work_img = ImageNormalizer.smart_resize(cropped_img, max_dim=max_sd_dim)
             # Resize mask to match
             h_work, w_work = work_img.shape[:2]
             work_mask = cv2.resize(cropped_mask, (w_work, h_work), interpolation=cv2.INTER_NEAREST)
        else:
             work_img = cropped_img
             work_mask = cropped_mask
             
        # Process Mask (Dilate/Feather)
        blur = feathering if feathering % 2 == 1 else feathering + 1
        work_mask = MaskProcessor.process(work_mask, blur_radius=blur)

        # 4. Predict
        pil_img = Image.fromarray(cv2.cvtColor(work_img, cv2.COLOR_BGR2RGB))
        pil_mask = Image.fromarray(work_mask)
        
        with torch.no_grad():
            output = self.pipe(
                prompt=prompt or "high resolution, realistic, seamless blend",
                image=pil_img,
                mask_image=pil_mask,
                strength=strength,
                num_inference_steps=30
            ).images[0]
            
        result_work = cv2.cvtColor(np.array(output), cv2.COLOR_RGB2BGR)
        
        # 5. Paste Back
        if needs_resize:
            # Resize result back to crop size
            result_crop = cv2.resize(result_work, (w_crop, h_crop), interpolation=cv2.INTER_LANCZOS4)
        else:
            result_crop = result_work
            
        final_img = ImageNormalizer.paste_back(img, result_crop, bbox)
        
        return final_img

InpainterFactory.register("sd", StableDiffusionRunner)
