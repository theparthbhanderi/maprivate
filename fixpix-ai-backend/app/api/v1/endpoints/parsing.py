from fastapi import APIRouter, UploadFile, File, Form, HTTPException, status
from app.services.image_service import image_service
from app.engine.device import get_device
from app.engine.human_parsing.controller import human_parsing_controller
import logging
import time
import base64
import numpy as np
import cv2
from pydantic import BaseModel

router = APIRouter()
logger = logging.getLogger(__name__)

class ParsingResponse(BaseModel):
    colormap_image: str  # Base64 RGB
    parsing_map: str     # Base64 (mask png)
    masks: dict = {}     # { "face": "base64...", "hair": "base64..." }
    detected_parts: list = []
    processing_time_ms: float
    device: str

@router.post("/parse", response_model=ParsingResponse)
async def parse_human_endpoint(
    image: UploadFile = File(...),
    return_masks: bool = Form(True),
    merge_parts: bool = Form(False)
):
    """
    Parse Human Image into semantic parts (SCHP).
    """
    start_time = time.time()
    
    try:
        image_bytes = await image.read()
        
        from app.core.limits import usage_limits
        if len(image_bytes) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large")
        
        img = image_service.decode_image(image_bytes)
        
        from fastapi.concurrency import run_in_threadpool
        result = await run_in_threadpool(
            human_parsing_controller.parse, 
            img, 
            return_parts=return_masks, 
            merge_parts=merge_parts
        )
        
        duration = (time.time() - start_time) * 1000
        
        # Encode main images
        ctype_b64 = image_service.encode_image(result["color_map"], format=".png")
        map_b64 = image_service.encode_image(result["parsing_map"], format=".png")
        
        def b64_str(b):
            return base64.b64encode(b).decode("utf-8") if b else ""
            
        # Encode individual masks
        encoded_masks = {}
        if "masks" in result:
            for label, mask_arr in result["masks"].items():
                encoded_masks[label] = b64_str(image_service.encode_image(mask_arr, format=".png"))

        return ParsingResponse(
            colormap_image=b64_str(ctype_b64),
            parsing_map=b64_str(map_b64),
            masks=encoded_masks,
            detected_parts=result.get("parts", []),
            processing_time_ms=round(duration, 2),
            device=get_device().type
        )
        
    except Exception as e:
        logger.error(f"Parsing failed: {e}")
        raise HTTPException(status_code=500, detail=f"Parsing failed: {str(e)}")
