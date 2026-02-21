import logging
import numpy as np
import cv2
from PIL import Image
import torch
import asyncio

from app.engine.loader import model_manager
from app.engine.device import get_device

# Import our new modules
from app.engine.generative.sd_base_loader import SDBaseLoader
from app.engine.generative.sdxl_loader import SDXLLoader
from app.engine.generative.controlnet_manager import ControlNetManager
from app.engine.generative.prompt_engine import PromptEngine
from app.engine.generative.safety_filter import SafetyFilter

logger = logging.getLogger(__name__)

class GenerativeController:
    """
    Unified Controller for Generative AI.
    Orchestrates Loaders, Managers, and Inference.
    """
    
    def __init__(self):
        self.device = get_device()
        self.controlnet_manager = ControlNetManager(self.device.type)
        self.sd_loader = SDBaseLoader(self.device.type)
        self.sdxl_loader = SDXLLoader(self.device.type)
        
        # Async lock for inference
        self.lock = asyncio.Lock()
        
    def _get_bucket_res(self, w: int, h: int, target_pixels: int = 512*512) -> tuple:
        """
        Snap dimensions to optimal aspect ratio bucket divisible by 8.
        Keeps similar pixel count to target_pixels (to manage VRAM usage).
        """
        if w == 0 or h == 0: return 512, 512
        
        aspect = w / h
        
        # Calculate new_w * new_h ~= target_pixels
        # new_w / new_h = aspect
        # new_w = aspect * new_h
        # aspect * new_h^2 = target_pixels
        # new_h = sqrt(target_pixels / aspect)
        
        new_h = int(np.sqrt(target_pixels / aspect))
        new_w = int(aspect * new_h)
        
        # Snap to 8
        new_w = (new_w // 8) * 8
        new_h = (new_h // 8) * 8
        
        return new_w, new_h

    async def generate(self,
                 prompt: str,
                 negative_prompt: str = "",
                 image: np.ndarray = None, 
                 mask: np.ndarray = None, 
                 control_type: str = None,
                 control_image: np.ndarray = None,
                 strength: float = 0.75,
                 steps: int = 30,
                 guidance: float = 7.5,
                 seed: int = -1,
                 quality: str = "fast" # 'fast' (SD1.5) or 'pro' (SDXL)
                 ) -> dict:
        """
        Unified Generation Interface.
        Returns: { "image": np.ndarray } or raises Exception.
        """
        
        async with self.lock:
            # 0. Optimization: Trigger VRAM cleanup
            # We are entering Heavy Generation Mode. Clear the deck.
            # In a real shared env, this ensures SD has max room.
            model_manager.unload_all(exclude=["generative_sd"])

            # 1. Safety Check & Limits
            if not SafetyFilter.check_prompt(prompt):
                 raise ValueError("Unsafe prompt detected.")
            if steps > 50: steps = 50 # Optimization: Hard cap
            
            # 2. Prompt Engineering
            final_prompt = PromptEngine.enhance_prompt(prompt, quality_mode=quality)
            final_negative = PromptEngine.get_negative_prompt(negative_prompt)
            
            # 3. Mode Determination
            task_type = "txt2img"
            if mask is not None and image is not None:
                task_type = "inpainting"
            elif image is not None and control_type is None:
                task_type = "img2img"
            
            logger.info(f"Gen Request: {task_type}, Control={control_type}, Quality={quality}")
            
            # 4. ControlNet Loading (if needed)
            controlnet = None
            if control_type:
                controlnet = self.controlnet_manager.get_controlnet(control_type)
                
            # 5. Pipeline Loading
            pipeline = None
            if quality == "pro":
                pipeline = self.sdxl_loader.load_pipeline(task_type, controlnet)
                if not pipeline: # Fallback
                     logger.warning("SDXL failed to load. Falling back to SD 1.5")
                     pipeline = self.sd_loader.load_pipeline(task_type, controlnet)
            else:
                pipeline = self.sd_loader.load_pipeline(task_type, controlnet)
                
            # 6. Execution (Mock vs Real)
            if pipeline == "MOCK_PIPELINE" or pipeline is None:
                return self._mock_generation(image, mask, prompt)
                
            # 7. Preprocess Inputs (Numpy -> PIL + Dynamic Resizing)
            pil_image = None
            pil_mask = None
            pil_control = None
            
            width, height = 512, 512 # Default
            
            if image is not None:
                h, w = image.shape[:2]
                # Smart Bucket Resize
                width, height = self._get_bucket_res(w, h, target_pixels=512*512)
                
                # Resize input standard
                image_resized = cv2.resize(image, (width, height), interpolation=cv2.INTER_AREA)
                pil_image = Image.fromarray(cv2.cvtColor(image_resized, cv2.COLOR_BGR2RGB))
                
                if mask is not None:
                    mask_resized = cv2.resize(mask, (width, height), interpolation=cv2.INTER_NEAREST)
                    pil_mask = Image.fromarray(mask_resized)
                    
            if control_image is not None:
                # Control image should match target resolution
                h, w = control_image.shape[:2]
                if image is None:
                    # If T2I with control, control dictates aspect
                    width, height = self._get_bucket_res(w, h, target_pixels=512*512)
                    
                control_resized = cv2.resize(control_image, (width, height), interpolation=cv2.INTER_AREA)
                pil_control = Image.fromarray(cv2.cvtColor(control_resized, cv2.COLOR_BGR2RGB))
            
            # If standard T2I (no inputs), defaults 512x512 from logic above apply
            
            generator = None
            if seed != -1:
                generator = torch.Generator(self.device.type).manual_seed(seed)
                
            # Inference Args Construction
            gen_args = {
                "prompt": final_prompt,
                "negative_prompt": final_negative,
                "num_inference_steps": steps,
                "guidance_scale": guidance,
                "generator": generator
            }
            
            if task_type == "txt2img":
                if controlnet:
                    gen_args["image"] = pil_control if pil_control else pil_image
                    # Width/Height inferred from image
                else:
                    gen_args["height"] = height
                    gen_args["width"] = width
                    
            elif task_type == "img2img":
                if controlnet:
                     # Complex case, simplified: T2I with Control
                     # Or pass 'image' as init and 'control_image' as control if pipe supports
                     # For MVP we treat as T2I with Control if Control is present, ignoring init strength
                     # Or we need Multi-Control pipe
                     gen_args["image"] = pil_control
                else:
                    gen_args["image"] = pil_image
                    gen_args["strength"] = strength
                    
            elif task_type == "inpainting":
                gen_args["image"] = pil_image
                gen_args["mask_image"] = pil_mask
                gen_args["height"] = height
                gen_args["width"] = width
                if controlnet:
                    # Handled by mock/complex pipe (not fully implemented in SDBase yet for combo)
                    pass
                    
            # Run
            logger.info(f"Running inference at {width}x{height}...")
            # try/except OOM -> auto downscale could be added here
            with torch.inference_mode():
                output = pipeline(**gen_args).images[0]
                
            # Post-process (PIL -> Numpy)
            res_np = np.array(output)
            res_np = cv2.cvtColor(res_np, cv2.COLOR_RGB2BGR)
            
            return {
                "image": res_np
            }

    def _mock_generation(self, image, mask, prompt):
        """Mock output for UI dev."""
        import time
        time.sleep(1.5)
        h, w = 512, 512
        if image is not None:
            h, w = image.shape[:2]
            
        out = np.zeros((h, w, 3), dtype=np.uint8)
        # Gradient
        for i in range(h):
            out[i, :, 0] = int(i/h*255)
            out[i, :, 1] = 100
            out[i, :, 2] = 200
            
        cv2.putText(out, "Generative AI (Mock)", (50, h//2), cv2.FONT_HERSHEY_SIMPLEX, 1, (255,255,255), 2)
        cv2.putText(out, f"Prompt: {prompt[:15]}...", (50, h//2 + 40), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255,255,255), 1)
        
        return {"image": out}


# Global Instance
generative_controller = GenerativeController()
