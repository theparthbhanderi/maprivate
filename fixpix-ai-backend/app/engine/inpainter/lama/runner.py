import os
import cv2
import numpy as np
import logging
from app.engine.inpainter.base import InpainterModel
from app.engine.inpainter.factory import InpainterFactory
from app.engine.utils.normalizer import ImageNormalizer
from app.engine.inpainter.mask_processor import MaskProcessor
from app.core.config import settings

logger = logging.getLogger(__name__)

class LaMaRunner(InpainterModel):
    def load(self) -> None:
        logger.info("Loading LaMa Inpainting model...")
        self.model = "MOCK_LAMA" # Replace with actual TorchScript loading
        # if os.path.exists(path): self.model = torch.jit.load(path)
        self.is_loaded = True
        logger.info("LaMa model loaded.")

    def predict(self, img: np.ndarray, mask: np.ndarray, prompt: str = "", strength: float = 1.0, feathering: int = 9, **kwargs) -> np.ndarray:
        if not self.is_loaded:
            self.load()
            
        logger.info(f"Running LaMa Inpainting (Strength={strength}, Feather={feathering})...")
        
        # 1. Process Mask
        # Map feathering (px) to blur radius (odd number)
        blur = feathering if feathering % 2 == 1 else feathering + 1
        mask = MaskProcessor.process(mask, blur_radius=blur)
        
        # 2. Smart Crop optimization
        # LaMa works best on 512x512. If the image is 4K, downscaling the whole thing loses detail.
        # Instead, we crop around the mask, inpaint that crop, and paste back.
        
        cropped_img, cropped_mask, bbox = ImageNormalizer.crop_to_mask(img, mask, padding=100)
        
        # 3. Predict on Crop
        if self.model == "MOCK_LAMA":
            # Mock: Telea
            result_crop = cv2.inpaint(cropped_img, cropped_mask, 3, cv2.INPAINT_TELEA)
        else:
            # Real Inference
            # tensor_img = normalize(cropped_img)
            # tensor_mask = normalize(cropped_mask)
            # output = self.model(tensor_img, tensor_mask)
            # result_crop = denormalize(output)
            pass
            
        # 4. Paste Back
        # Only paste if we essentially mocked it or have a real result
        if "result_crop" in locals():
            final_img = ImageNormalizer.paste_back(img, result_crop, bbox)
            return final_img
            
        return img

InpainterFactory.register("lama", LaMaRunner)
