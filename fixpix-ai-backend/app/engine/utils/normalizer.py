import cv2
import numpy as np
import logging

logger = logging.getLogger(__name__)

class ImageNormalizer:
    @staticmethod
    def smart_resize(img: np.ndarray, max_dim: int = 2048) -> np.ndarray:
        """
        Downscale image if larger than max_dim to prevent OOM.
        """
        h, w = img.shape[:2]
        if max(h, w) > max_dim:
            logger.info(f"Smart Resize: {w}x{h} -> Max {max_dim}")
            scale = max_dim / max(h, w)
            new_w = int(w * scale)
            new_h = int(h * scale)
            
            # Ensure divisible by 8 for AI models (UNets usually require this)
            new_w = (new_w // 8) * 8
            new_h = (new_h // 8) * 8
            
            img = cv2.resize(img, (new_w, new_h), interpolation=cv2.INTER_AREA)
        else:
             # Ensure dimensions are divisible by 8 even if not resizing
             new_w = (w // 8) * 8
             new_h = (h // 8) * 8
             if new_w != w or new_h != h:
                 img = cv2.resize(img, (new_w, new_h), interpolation=cv2.INTER_AREA)
                 
        return img
    
    @staticmethod
    def crop_to_mask(img: np.ndarray, mask: np.ndarray, padding: int = 50) -> tuple:
        """
        Crop the image to the bounding box of the mask + padding.
        Returns (cropped_img, cropped_mask, bbox)
        bbox = (x, y, w, h)
        """
        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        if not contours:
            return img, mask, (0, 0, img.shape[1], img.shape[0])
            
        # Get bounding box of all contours
        x_min, y_min = np.inf, np.inf
        x_max, y_max = -np.inf, -np.inf
        
        for cnt in contours:
            x, y, w, h = cv2.boundingRect(cnt)
            x_min = min(x_min, x)
            y_min = min(y_min, y)
            x_max = max(x_max, x + w)
            y_max = max(y_max, y + h)
            
        # Add padding
        h_img, w_img = img.shape[:2]
        x_min = max(0, int(x_min - padding))
        y_min = max(0, int(y_min - padding))
        x_max = min(w_img, int(x_max + padding))
        y_max = min(h_img, int(y_max + padding))
        
        # Ensure divisible by 8 dimensions for the crop
        crop_w = x_max - x_min
        crop_h = y_max - y_min
        
        crop_w = (crop_w // 8) * 8
        crop_h = (crop_h // 8) * 8
        
        x_max = x_min + crop_w
        y_max = y_min + crop_h
        
        cropped_img = img[y_min:y_max, x_min:x_max]
        cropped_mask = mask[y_min:y_max, x_min:x_max]
        
        return cropped_img, cropped_mask, (x_min, y_min, crop_w, crop_h)

    @staticmethod
    def paste_back(original: np.ndarray, cropped: np.ndarray, bbox: tuple) -> np.ndarray:
        """
        Paste the cropped result back into the original image.
        """
        x, y, w, h = bbox
        # Resize cropped if it slightly mismatches due to processing (shouldn't if logic is correct)
        if cropped.shape[:2] != (h, w):
            cropped = cv2.resize(cropped, (w, h))
            
        result = original.copy()
        result[y:y+h, x:x+w] = cropped
        return result

    @staticmethod
    def get_crop_around_points(img_shape: tuple, points: list, padding: int = 100, min_size: int = 512) -> tuple:
        """
        Calculate crop box around points.
        Returns: (crop_box, relative_points)
        crop_box = (x, y, w, h)
        relative_points = [[rel_x, rel_y], ...]
        """
        h_img, w_img = img_shape[:2]
        
        if not points:
            return (0, 0, w_img, h_img), []
            
        x_min, y_min = np.inf, np.inf
        x_max, y_max = -np.inf, -np.inf
        
        for p in points:
            px, py = p[0], p[1]
            x_min = min(x_min, px)
            y_min = min(y_min, py)
            x_max = max(x_max, px)
            y_max = max(y_max, py)
            
        # Add padding
        x_min = max(0, int(x_min - padding))
        y_min = max(0, int(y_min - padding))
        x_max = min(w_img, int(x_max + padding))
        y_max = min(h_img, int(y_max + padding))
        
        # Ensure min size
        curr_w = x_max - x_min
        curr_h = y_max - y_min
        
        if curr_w < min_size:
            diff = min_size - curr_w
            x_min = max(0, x_min - diff // 2)
            x_max = min(w_img, x_max + diff // 2)
            
        if curr_h < min_size:
            diff = min_size - curr_h
            y_min = max(0, y_min - diff // 2)
            y_max = min(h_img, y_max + diff // 2)
            
        # Divisible by 8?
        w = x_max - x_min
        h = y_max - y_min
        w = (w // 8) * 8
        h = (h // 8) * 8
        x_max = x_min + w
        y_max = y_min + h
        
        crop_box = (x_min, y_min, w, h)
        
        # Calculate relative points
        rel_points = []
        for p in points:
            rel_points.append([p[0] - x_min, p[1] - y_min])
            
        return crop_box, rel_points
