import logging
import numpy as np
import cv2
from app.engine.loader import model_manager
from app.engine.human_parsing.schp.runner import LIP_LABELS

logger = logging.getLogger(__name__)

class HumanParsingController:
    """
    Controller for Human Parsing tasks.
    """
    
    def parse(self, image: np.ndarray, return_parts: bool = True, merge_parts: bool = False) -> dict:
        """
        Parse human image.
        Uses "Smart Crop": Runs RMBG first to detect person, crops to them, then runs SCHP.
        """
        schp_model = model_manager.get_model("human_parsing_schp")
        
        # Optimization: Try Smart Crop if possible
        # Check if we have RMBG model loaded or available
        # We can try to load it just for detection
        
        parsing_map_full = None
        
        try:
            # 1. Detect Person (RMBG)
            # We access the segmentation_rmbg model directly if registered
            # Or simplified: just assume we want full image parsing if we can't find RMBG
            # But let's try to get it.
            
            # 1. Detect Person (RMBG)
            # Force load RMBG model for detection
            rmbg_model = model_manager.get_model("segmentation_rmbg")

            # IF we have RMBG, use it.
            if rmbg_model:
                logger.info("HumanParsing: Running Smart Crop (RMBG -> SCHP)...")
                
                # Run RMBG (fast)
                # RMBG output is RGBA
                rmbg_result = rmbg_model.predict(image)
                mask = rmbg_result[:, :, 3] # Alpha channel
                
                # Check if person found (if mask is empty, fallback)
                if np.max(mask) > 0:
                   from app.engine.utils.normalizer import ImageNormalizer
                   
                   # 2. Crop
                   img_crop, _, bbox = ImageNormalizer.crop_to_mask(image, mask, padding=50)
                   
                   # 3. Parse Crop
                   # Ensure crop isn't too tiny
                   if img_crop.shape[0] > 64 and img_crop.shape[1] > 64:
                       parsing_map_crop, labels_crop = schp_model.predict(img_crop)
                       
                       # 4. Paste Back
                       parsing_map_full = np.zeros(image.shape[:2], dtype=np.uint8)
                       # Need integer paste logic. ImageNormalizer.paste_back is for images (3 channels usually)
                       # Let's write simple paste logic here or reuse
                       
                       x, y, w, h = bbox
                       # Resize crop result to fit bbox if needed (SCHP might resize internally?)
                       # SCHP output matches input size usually.
                       
                       # If SCHP resized it, we must align
                       if parsing_map_crop.shape[:2] != (h, w):
                           # Nearest neighbor for labels
                           parsing_map_crop = cv2.resize(parsing_map_crop, (w, h), interpolation=cv2.INTER_NEAREST)
                           
                       parsing_map_full[y:y+h, x:x+w] = parsing_map_crop
                       labels = labels_crop
                   else:
                       logger.warning("Person crop too small, falling back to full image.")
        except Exception as e:
            logger.warning(f"Smart Crop failed: {e}. Falling back to full image.")
            
        # Fallback: Run on full image if optimization failed or skipped
        if parsing_map_full is None:
            logger.info("HumanParsing: Running Standard (Full Image)...")
            parsing_map_full, labels = schp_model.predict(image)

        parsing_map = parsing_map_full
        
        # Generator Color Map
        color_map = self.visualize_parsing(parsing_map)
        
        result = {
            "parsing_map": parsing_map,
            "color_map": color_map,
            "labels": labels,
            "parts": []
        }
        
        if return_parts:
            # Extract individual binary masks
            from app.engine.human_parsing.utils.postprocess import ParsingPostprocessor
            masks = ParsingPostprocessor.extract_masks(parsing_map, labels, merge_parts=merge_parts)
            result["masks"] = masks
            result["parts"] = list(masks.keys())
            
        return result

    def visualize_parsing(self, parsing_map: np.ndarray) -> np.ndarray:
        """
        Convert label map to colored image.
        """
        # Create a color palette (random but deterministic)
        np.random.seed(42)
        palette = np.random.randint(0, 255, (20, 3), dtype=np.uint8)
        palette[0] = [0, 0, 0] # Background is black
        
        h, w = parsing_map.shape
        color_map = np.zeros((h, w, 3), dtype=np.uint8)
        
        for label_id in range(1, 20):
            color_map[parsing_map == label_id] = palette[label_id]
            
        return color_map

human_parsing_controller = HumanParsingController()
