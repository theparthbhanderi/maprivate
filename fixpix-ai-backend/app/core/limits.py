import psutil
import torch
import cv2
import numpy as np
from fastapi import HTTPException
from app.core.config import settings
import logging

logger = logging.getLogger(__name__)

class UsageLimits:
    MAX_PIXELS = 4096 * 4096  # 4K resolution limit
    MAX_FILE_SIZE = 15 * 1024 * 1024  # 15MB

    @staticmethod
    def validate_image(img_bytes: bytes, img: np.ndarray):
        """
        Validate image size limits.
        Raises HTTPException if invalid.
        """
        if len(img_bytes) > UsageLimits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large (max 15MB)")
             
        h, w = img.shape[:2]
        if h * w > UsageLimits.MAX_PIXELS:
            raise HTTPException(status_code=400, detail="Image resolution too high (max 4K)")

class MemoryWatchdog:
    MIN_RAM_MB = 500
    
    @staticmethod
    def check_resources():
        """
        Check if system has enough resources to proceed.
        Triggers GC if low.
        Returns False if critical.
        """
        # 1. System RAM
        mem = psutil.virtual_memory()
        available_mb = mem.available / (1024 * 1024)
        
        if available_mb < MemoryWatchdog.MIN_RAM_MB:
            logger.warning(f"Low RAM: {available_mb}MB. Triggering GC.")
            import gc
            gc.collect()
            
            # Re-check
            mem = psutil.virtual_memory()
            available_mb = mem.available / (1024 * 1024)
            if available_mb < MemoryWatchdog.MIN_RAM_MB:
                return False
                
        # 2. VRAM (if CUDA)
        if torch.cuda.is_available():
            try:
                # Basic check - if OOM is imminent, usually it happens during alloc
                # But we can empty cache proactively
                torch.cuda.empty_cache()
            except Exception:
                pass
                
        return True

usage_limits = UsageLimits()
memory_watchdog = MemoryWatchdog()
