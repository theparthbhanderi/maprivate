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

class RestorationResponse(BaseModel):
    restored_image: str  # Base64 encoded
    face_count: int
    processing_time_ms: float
    device: str
    mode: str

@router.post("/restore", response_model=RestorationResponse)
async def restore_face_endpoint(
    file: UploadFile = File(...),
    mode: str = Form("fast"),  # 'fast' or 'quality'
    upscale: bool = Form(True),
    fidelity: float = Form(0.5)
):
    """
    Face Restoration Endpoint.
    - **mode**: 'fast' (GFPGAN) or 'quality' (CodeFormer)
    - **upscale**: Whether to upscale the result
    - **fidelity**: Only for quality mode (0.0=enhance, 1.0=identity)
    """
    if mode not in ["fast", "quality"]:
        raise HTTPException(status_code=400, detail="Invalid mode. Use 'fast' or 'quality'.")
    
    start_time = time.time()
    
    try:
        # Read file (Async I/O)
        contents = await file.read()
        
        # Validation
        if len(contents) == 0:
             raise HTTPException(status_code=400, detail="Empty file provided.")
             
        # Reuse decode from service to check dimensions quickly? 
        # Ideally we check bytes size first
        from app.core.limits import usage_limits
        # We need numpy image for dimension check, ImageService decodes it
        # We'll let ImageService handle decode, but validate bytes length first
        if len(contents) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large (max 15MB)")
             
        logger.info(f"Request: mode={mode}, size={len(contents)} bytes")
        
        # Run inference via JobQueue (Controlled Concurrency)
        from app.engine.queue_manager import job_queue
        
        # We wrap the service call
        result = await job_queue.run_job(
            image_service.restore_face, 
            image_data=contents, 
            mode=mode, 
            fidelity=fidelity
        )
        
        duration = (time.time() - start_time) * 1000
        device_name = get_device().type
        
        # Encode bytes to Base64 for JSON response
        b64_image = base64.b64encode(result["image"]).decode("utf-8")
        
        return RestorationResponse(
            restored_image=b64_image,
            face_count=result["face_count"],
            processing_time_ms=round(duration, 2),
            device=device_name,
            mode=mode
        )
        
    except Exception as e:
        logger.error(f"Restoration failed: {e}")
        raise HTTPException(status_code=500, detail=f"Processing failed: {str(e)}")
