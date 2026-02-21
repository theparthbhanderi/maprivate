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

class ColorizeResponse(BaseModel):
    colorized_image: str  # Base64
    processing_time_ms: float
    device: str
    render_factor: int
    mode: str

@router.post("/colorize", response_model=ColorizeResponse)
async def colorize_endpoint(
    image: UploadFile = File(...),
    mode: str = Form("artistic"), # 'artistic' or 'stable'
    render_factor: int = Form(35),
    enhance_faces: bool = Form(False)
):
    """
    Colorize B&W Image.
    - **mode**: 'artistic' (Vibrant) or 'stable' (Realistic)
    - **render_factor**: 10-45.
    - **enhance_faces**: Run GFPGAN on faces after colorization for detail.
    """
    start_time = time.time()
    
    try:
        # Read files
        image_bytes = await image.read()
        
        # Validation
        from app.core.limits import usage_limits
        if len(image_bytes) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large")
        
        logger.info(f"Colorize Request: factor={render_factor}")
        
        # Run inference via JobQueue
        from app.engine.queue_manager import job_queue
        
        result = await job_queue.run_job(
            image_service.colorize_image, 
            image_data=image_bytes, 
            mode=mode,
            render_factor=render_factor,
            enhance_faces=enhance_faces
        )
        
        duration = (time.time() - start_time) * 1000
        
        b64_image = base64.b64encode(result["image"]).decode("utf-8")
        
        return ColorizeResponse(
            colorized_image=b64_image,
            processing_time_ms=round(duration, 2),
            device=result.get("device", "unknown"),
            render_factor=result["render_factor"],
            mode=result["mode"]
        )
        
    except Exception as e:
        logger.error(f"Colorization failed: {e}")
        raise HTTPException(status_code=500, detail=f"Colorization failed: {str(e)}")
