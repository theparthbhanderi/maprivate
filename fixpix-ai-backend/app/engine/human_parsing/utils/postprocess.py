import cv2
import numpy as np
import logging
from app.engine.human_parsing.schp.runner import LIP_LABELS

logger = logging.getLogger(__name__)

class ParsingPostprocessor:
    """
    Utilities for processing raw parsing maps into usable masks.
    """
    
    @staticmethod
    def extract_masks(parsing_map: np.ndarray, labels: dict = LIP_LABELS, merge_parts: bool = False) -> dict:
        """
        Extract binary masks for each semantic part.
        Args:
            merge_parts: If True, merges bilateral parts (Left-arm + Right-arm -> Arms)
        """
        masks = {}
        h, w = parsing_map.shape
        present_ids = np.unique(parsing_map)
        
        # Mapping for merging
        # e.g. "Left-shoe" (18) -> "shoes", "Right-shoe" (19) -> "shoes"
        merge_map = {
            14: "arms", 15: "arms",
            16: "legs", 17: "legs",
            18: "shoes", 19: "shoes"
        }
        
        for pid in present_ids:
            if pid == 0: continue # Skip background
            
            label_name = labels.get(pid, f"class_{pid}").lower().replace("-", "_")
            
            if merge_parts and pid in merge_map:
                target_name = merge_map[pid]
                if target_name not in masks:
                    masks[target_name] = np.zeros((h, w), dtype=np.uint8)
                masks[target_name][parsing_map == pid] = 255
            else:
                # Standard extraction
                # If merging is ON, we might want to rename "left_arm" to "arms" if it's the only one?
                # But logic above handles accumulation.
                # Non-mergable parts (Face, Hair, etc) or if merge is OFF
                
                # Careful: if merge is on, 'masks' keys are merged names.
                # if merge is off, keys are 'left_shoe'.
                
                target_name = label_name
                if merge_parts:
                     # Skip if already handled by merge map
                     if pid in merge_map: continue

                masks[target_name] = np.zeros((h, w), dtype=np.uint8)
                masks[target_name][parsing_map == pid] = 255
                
        return masks

    @staticmethod
    def merge_masks(parsing_map: np.ndarray, label_ids: list) -> np.ndarray:
        """
        Merge multiple parts into one mask (e.g. all clothes).
        """
        mask = np.zeros(parsing_map.shape, dtype=np.uint8)
        for pid in label_ids:
            mask[parsing_map == pid] = 255
        return mask
        
    @staticmethod
    def post_process_mask(mask: np.ndarray, smooth: bool = True) -> np.ndarray:
        """
        Smooth edges of the mask.
        """
        if smooth:
            # Morphological close to fill holes
            kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (3, 3))
            mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)
            
            # Gaussian blur for soft edges
            # mask = cv2.GaussianBlur(mask, (3, 3), 0)
        return mask
