import torch
import logging
from typing import Any, Optional

try:
    from diffusers import (
        StableDiffusionPipeline,
        StableDiffusionImg2ImgPipeline,
        StableDiffusionInpaintPipeline,
        StableDiffusionControlNetPipeline,
        DPMSolverMultistepScheduler,
        UniPCMultistepScheduler
    )
    DIFFUSERS_AVAILABLE = True
except ImportError:
    DIFFUSERS_AVAILABLE = False

logger = logging.getLogger(__name__)

class SDBaseLoader:
    """
    Loader for Stable Diffusion 1.5 based models.
    """
    
    MODEL_ID = "runwayml/stable-diffusion-v1-5"
    
    def __init__(self, device: str):
        self.device = device
        self.torch_dtype = torch.float16 if device == "cuda" else torch.float32
        
    def load_pipeline(self, 
                      task_type: str = "txt2img", 
                      controlnet=None) -> Any:
        """
        Load logical pipeline. 
        Note: Real efficient implementation shares components (UNet/VAE). 
        Diffusers 'AutoPipeline' does this well.
        For explicit control, we instantiate specifically.
        """
        if not DIFFUSERS_AVAILABLE:
            return "MOCK_PIPELINE"
            
        # Pipeline Selection
        # If controlnet is provided, we MUST use ControlNetPipeline
        
        args = {
            "pretrained_model_name_or_path": self.MODEL_ID,
            "torch_dtype": self.torch_dtype,
            "safety_checker": None # Disable for speed/memory in backend (we filter prompt)
        }
        
        try:
            if controlnet:
                # Text-to-Image with ControlNet
                # What about Img2Img with ControlNet? 
                # Diffusers `StableDiffusionControlNetPipeline` supports `image` arg which acts as control.
                # If we want I2I + Control, we supply 'image' (input) + 'control_image'.
                # Actually, standard ControlNetPipe is T2I guided by control.
                # For Inpainting + ControlNet, use StableDiffusionControlNetInpaintPipeline
                
                pipeline = StableDiffusionControlNetPipeline.from_pretrained(
                    self.MODEL_ID,
                    controlnet=controlnet,
                    **args
                )
                
            elif task_type == "inpainting":
                pipeline = StableDiffusionInpaintPipeline.from_pretrained(
                    self.MODEL_ID,
                    **args
                )
                
            elif task_type == "img2img":
                pipeline = StableDiffusionImg2ImgPipeline.from_pretrained(
                    self.MODEL_ID,
                    **args
                )
                
            else: # txt2img
                pipeline = StableDiffusionPipeline.from_pretrained(
                    self.MODEL_ID,
                    **args
                )

            # Scheduler Optimization (DPMSolver is fast ~20 steps)
            pipeline.scheduler = DPMSolverMultistepScheduler.from_config(
                pipeline.scheduler.config,
                use_karras_sigmas=True
            )
            
            # Memory Optimizations
            if self.device == "cuda":
                # pipeline.enable_model_cpu_offload() # Specific function available in newer diffusers
                pipeline.to("cuda")
                pipeline.enable_attention_slicing()
            elif self.device == "mps":
                pipeline.to("mps")
                # Attention slicing might help or hurt on MPS, verifying.
                # Usually helps memory.
                pipeline.enable_attention_slicing()
            else:
                pipeline.to("cpu")
                
            return pipeline
            
        except Exception as e:
            logger.error(f"Failed to load SD Base Pipeline: {e}")
            return None
