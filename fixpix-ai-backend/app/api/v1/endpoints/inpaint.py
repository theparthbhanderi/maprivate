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

class InpaintResponse(BaseModel):
    repaired_image: str  # Base64
    processing_time_ms: float
    device: str
    engine: str
    mode: str

@router.post("/inpaint", response_model=InpaintResponse)
async def inpaint_endpoint(
    image: UploadFile = File(...),
    mask: UploadFile = File(...),
    mode: str = Form("fast"), # 'fast' (LaMa) or 'smart' (SD)
    prompt: Optional[str] = Form(""),
    strength: float = Form(0.8),
    feathering: int = Form(9)
):
    """
    Inpainting Endpoint.
    - **mode**: 'fast' (LaMa) or 'smart' (SD)
    - **strength**: 0.0-1.0 (Integration strength / Denoising strength for SD)
    - **feathering**: Soften mask edges (pixels)
    """
    start_time = time.time()
    
    try:
        # Read files
        image_bytes = await image.read()
        mask_bytes = await mask.read()
        
        # Validation
        from app.core.limits import usage_limits
        if len(image_bytes) > usage_limits.MAX_FILE_SIZE:
             raise HTTPException(status_code=413, detail="Image too large")
        
        logger.info(f"Inpaint Request: mode={mode}, prompt='{prompt}', str={strength}, fth={feathering}")
        
        # Run inference via JobQueue
        from app.engine.queue_manager import job_queue
        
        result = await job_queue.run_job(
            image_service.inpaint_image, 
            image_data=image_bytes, 
            mask_data=mask_bytes,
            mode=mode,
            prompt=prompt,
            strength=strength,
            feathering=feathering
        )
        
        duration = (time.time() - start_time) * 1000
        
        b64_image = base64.b64encode(result["image"]).decode("utf-8")
        
        return InpaintResponse(
            repaired_image=b64_image,
            processing_time_ms=round(duration, 2),
            device=result.get("device", "unknown"),
            engine=result.get("engine", "unknown"),
            mode=result["mode"]
        )
        
    except Exception as e:
        logger.error(f"Inpaint failed: {e}")
        raise HTTPException(status_code=500, detail=f"Inpainting failed: {str(e)}")
