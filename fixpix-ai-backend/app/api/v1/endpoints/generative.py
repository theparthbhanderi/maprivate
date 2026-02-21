from fastapi import APIRouter, UploadFile, File, Form, HTTPException, BackgroundTasks
from fastapi.concurrency import run_in_threadpool
import logging
import time
import base64
import numpy as np
import cv2
from pydantic import BaseModel
from typing import Optional, List, Dict, Any

from app.services.image_service import image_service
from app.engine.device import get_device
from app.engine.generative.generative_controller import generative_controller
from app.core.limits import usage_limits

router = APIRouter()
logger = logging.getLogger(__name__)

class GenerativeResponse(BaseModel):
    image: str # Base64
    seed: int
    processing_time_ms: float
    device: str
    params: Dict[str, Any]

@router.post("/edit", response_model=GenerativeResponse)
async def generate_edit(
    image: Optional[UploadFile] = File(None),
    mask: Optional[UploadFile] = File(None),
    control_image: Optional[UploadFile] = File(None),
    prompt: str = Form(...),
    negative_prompt: str = Form(""),
    mode: str = Form("txt2img"), # txt2img, img2img, inpaint
    control_type: Optional[str] = Form(None),
    strength: float = Form(0.75),
    steps: int = Form(30),
    guidance: float = Form(7.5),
    seed: int = Form(-1),
    quality: str = Form("fast"), # fast (SD1.5) | pro (SDXL)
):
    """
    Generative Editing Endpoint.
    Supports: T2I, I2I, Inpainting, ControlNet.
    """
    start_time = time.time()
    
    # 1. Validation
    # Mode check logic?
    if mode == "inpaint" and (image is None or mask is None):
        raise HTTPException(status_code=400, detail="Inpainting requires both image and mask.")
    if mode == "img2img" and image is None:
        raise HTTPException(status_code=400, detail="Img2Img requires an input image.")
        
    # 2. Decode Images
    img_np = None
    mask_np = None
    control_np = None
    
    try:
        if image:
            content = await image.read()
            if len(content) > usage_limits.MAX_FILE_SIZE:
                raise HTTPException(status_code=413, detail="Input image too large.")
            img_np = image_service.decode_image(content)
            
        if mask:
            content = await mask.read()
            mask_np = image_service.decode_image(content)
            # Ensure mask is single channel or handle in controller
            if len(mask_np.shape) == 3:
                mask_np = cv2.cvtColor(mask_np, cv2.COLOR_BGR2GRAY)
                
        if control_image:
            content = await control_image.read()
            control_np = image_service.decode_image(content)
            
        # 3. Run Inference (Threadpool for non-blocking)
        # We pass numpy arrays to controller
        result = await run_in_threadpool(
            generative_controller.generate,
            prompt=prompt,
            negative_prompt=negative_prompt,
            image=img_np,
            mask=mask_np,
            control_type=control_type,
            control_image=control_np,
            strength=strength,
            steps=steps,
            guidance=guidance,
            seed=seed,
            quality=quality
        )
        
        # 4. Process Response
        duration = (time.time() - start_time) * 1000
        
        out_b64 = image_service.encode_image(result["image"], format=".png")
        b64_str = base64.b64encode(out_b64).decode("utf-8")
        
        return GenerativeResponse(
            image=b64_str,
            seed=result.get("seed", seed), # Controller might return used seed if random
            processing_time_ms=round(duration, 2),
            device=get_device().type,
            params={
                "mode": mode,
                "quality": quality,
                "control_type": control_type
            }
        )
        
    except ValueError as ve:
        raise HTTPException(status_code=400, detail=str(ve))
    except Exception as e:
        logger.error(f"Generation failed: {e}")
        # Cleanup if needed
        raise HTTPException(status_code=500, detail=f"Generation process failed: {str(e)}")
