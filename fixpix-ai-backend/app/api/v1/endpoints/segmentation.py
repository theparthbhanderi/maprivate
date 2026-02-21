from fastapi import APIRouter, UploadFile, File, Form, HTTPException, status
from fastapi.concurrency import run_in_threadpool
from app.services.image_service import image_service
from app.engine.device import get_device
import logging
import time
import base64
from pydantic import BaseModel
from typing import Optional

router = APIRouter()
logger = logging.getLogger(__name__)

class SegmentationResponse(BaseModel):
    cutout_image: str     # Base64 RGBA
    mask_image: str       # Base64 Mask
    preview_image: Optional[str] = None
    processing_time_ms: float
    device: str
    mode: str
    feather: int

@router.post("/segment", response_model=SegmentationResponse)
async def segment_endpoint(
    image: UploadFile = File(...),
    mode: str = Form("auto"), # 'auto' | 'refine'
    prompts: Optional[str] = Form(None), # JSON string: points/boxes
    feather: int = Form(0), # 0-40 px
    return_type: str = Form("all") # transparent | mask | cutout | all
):
    """
    Remove Background / Segment Image.
    - **mode**: 'auto' (RMBG) or 'refine' (SAM Interactive).
    - **prompts**: JSON string for SAM (e.g. `{"points": [[x,y]]}`).
    - **feather**: Pixel blur for edges (0-40).
    """
    start_time = time.time()
    
    try:
        image_bytes = await image.read()
        
        # Validation
        from app.core.limits import usage_limits
        if len(image_bytes) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large")
        
        logger.info(f"Segmentation Request: Mode={mode}, Feather={feather}")
        
        from app.engine.queue_manager import job_queue
        
        result = await job_queue.run_job(
            image_service.segment_image, 
            image_data=image_bytes, 
            mode=mode,
            prompts=prompts,
            feather=feather,
            return_type=return_type
        )
        
        duration = (time.time() - start_time) * 1000
        
        # Decode components from service result (which are bytes) to Base64 strings for JSON response
        def b64_str(b):
            return base64.b64encode(b).decode("utf-8") if b else ""

        return SegmentationResponse(
            cutout_image=b64_str(result.get("cutout_image")),
            mask_image=b64_str(result.get("mask_image")),
            preview_image=b64_str(result.get("preview_image")),
            processing_time_ms=round(duration, 2),
            device=result.get("device", "unknown"),
            mode=result["mode"],
            feather=result.get("feather", 0)
        )
        
    except Exception as e:
        logger.error(f"Segmentation failed: {e}")
        raise HTTPException(status_code=500, detail=f"Segmentation failed: {str(e)}")
