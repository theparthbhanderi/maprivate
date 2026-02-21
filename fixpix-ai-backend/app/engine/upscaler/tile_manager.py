import math
import cv2
import numpy as np
import torch
import logging

logger = logging.getLogger(__name__)

class TileManager:
    """
    Handles splitting images into tiles for processing and merging them back.
    Vital for preventing OOM on GPUs when upscaling large images.
    """
    def __init__(self, tile_size=400, tile_pad=10):
        self.tile_size = tile_size
        self.tile_pad = tile_pad

    def process_with_tiling(self, img: np.ndarray, model_func, scale: int = 4) -> np.ndarray:
        """
        Process the image using tiling.
        img: Input image (H, W, C) - BGR or RGB (assumed consistent with model)
        model_func: Function to call for inference on a tile (tensor/array -> tensor/array)
        scale: Upscaling factor
        """
        h, w, c = img.shape
        
        # If image is small enough, skip tiling
        if h < self.tile_size and w < self.tile_size:
            return model_func(img)

        # Output dimensions
        h_out, w_out = h * scale, w * scale
        output_img = np.zeros((h_out, w_out, c), dtype=np.uint8)
        
        # Grid calculation
        tiles_x = math.ceil(w / self.tile_size)
        tiles_y = math.ceil(h / self.tile_size)
        
        logger.info(f"Tiling enabled: {tiles_x}x{tiles_y} grid ({tiles_x*tiles_y} tiles)")

        for y in range(tiles_y):
            for x in range(tiles_x):
                # Calculate coordinates
                y_start = y * self.tile_size
                y_end = min(y_start + self.tile_size, h)
                x_start = x * self.tile_size
                x_end = min(x_start + self.tile_size, w)
                
                # Add padding
                y_pad_start = max(0, y_start - self.tile_pad)
                y_pad_end = min(h, y_end + self.tile_pad)
                x_pad_start = max(0, x_start - self.tile_pad)
                x_pad_end = min(w, x_end + self.tile_pad)

                # Extract tile
                tile = img[y_pad_start:y_pad_end, x_pad_start:x_pad_end, :]

                # Process tile
                try:
                    with torch.no_grad():
                        processed_tile = model_func(tile)
                except RuntimeError as e:
                    if "out of memory" in str(e).lower():
                        logger.error("OOM during tile processing. Try reducing tile_size.")
                        raise e
                    raise e

                # Calculate output coordinates
                out_y_start = y_start * scale
                out_x_start = x_start * scale
                
                # Calculate valid region within the processed tile (removing padding)
                # Input-relative offsets
                rel_y_start = (y_start - y_pad_start) * scale
                rel_x_start = (x_start - x_pad_start) * scale
                rel_y_end = rel_y_start + (y_end - y_start) * scale
                rel_x_end = rel_x_start + (x_end - x_start) * scale
                
                # Safeguard dimensions
                if processed_tile is None:
                    continue

                # Crop valid area from processed tile
                valid_output = processed_tile[rel_y_start:rel_y_end, rel_x_start:rel_x_end, :]
                
                # Place in output
                h_valid, w_valid = valid_output.shape[:2]
                output_img[out_y_start:out_y_start+h_valid, out_x_start:out_x_start+w_valid, :] = valid_output

        return output_img
