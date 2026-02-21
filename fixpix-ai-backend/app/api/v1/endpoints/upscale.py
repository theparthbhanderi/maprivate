from fastapi import APIRouter, UploadFile, File, Form, HTTPException, status
from fastapi.concurrency import run_in_threadpool
from app.services.image_service import image_service
from app.engine.device import get_device
import logging
import time
import base64
from pydantic import BaseModel

router = APIRouter()
logger = logging.getLogger(__name__)

class SuperResolutionResponse(BaseModel):
    upscaled_image: str  # Base64
    processing_time_ms: float
    device: str
    scale: int
    original_resolution: str
    new_resolution: str
    mode: str

@router.post("/super-resolution", response_model=SuperResolutionResponse)
async def super_resolution_endpoint(
    file: UploadFile = File(...),
    scale: int = Form(4),
    enhance_faces: bool = Form(False),
    fast_mode: bool = Form(False)
):
    """
    Super Resolution Endpoint.
    - **scale**: 2 or 4 (default 4)
    - **enhance_faces**: Whether to enhance faces in the image
    - **fast_mode**: If enhance_faces is True, use fast mode (GFPGAN) vs Quality (CodeFormer)
    """
    start_time = time.time()
    
    try:
        if scale not in [2, 4]:
            raise HTTPException(status_code=400, detail="Scale must be 2 or 4.")

        # Read file (Async I/O)
        contents = await file.read()
        
        # Validation
        from app.core.limits import usage_limits
        if len(contents) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large")
        
        logger.info(f"Super-Res Request: scale={scale}, faces={enhance_faces}, fast={fast_mode}, size={len(contents)} bytes")
        
        # Run inference via JobQueue
        from app.engine.queue_manager import job_queue
        
        result = await job_queue.run_job(
            image_service.upscale_image, 
            image_data=contents, 
            scale=scale,
            enhance_faces=enhance_faces,
            fast_mode=fast_mode
        )
        
        duration = (time.time() - start_time) * 1000
        device_name = get_device().type
        
        b64_image = base64.b64encode(result["image"]).decode("utf-8")
        
        return SuperResolutionResponse(
            upscaled_image=b64_image,
            processing_time_ms=round(duration, 2),
            device=device_name,
            scale=result["scale"],
            original_resolution=result["original_resolution"],
            new_resolution=result["new_resolution"],
            mode=result["mode"]
        )
        
    except Exception as e:
        logger.error(f"Super-Res failed: {e}")
        raise HTTPException(status_code=500, detail=f"Processing failed: {str(e)}")
