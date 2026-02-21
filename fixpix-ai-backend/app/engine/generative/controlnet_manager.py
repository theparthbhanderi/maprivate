import logging
import torch
from typing import Optional, Any

# Mock vs Real toggle
TRY_REAL_IMPORT = True

if TRY_REAL_IMPORT:
    try:
        from diffusers import ControlNetModel
        DIFFUSERS_AVAILABLE = True
    except ImportError:
        DIFFUSERS_AVAILABLE = False
else:
    DIFFUSERS_AVAILABLE = False

logger = logging.getLogger(__name__)

class ControlNetManager:
    """
    Manages ControlNet models loading and selection.
    """
    
    MODEL_MAP = {
        "canny": "lllyasviel/sd-controlnet-canny",
        "pose": "lllyasviel/sd-controlnet-openpose",
        "depth": "lllyasviel/sd-controlnet-depth",
        "scribble": "lllyasviel/sd-controlnet-scribble",
        "seg": "lllyasviel/sd-controlnet-seg", # Segmentation
        "softedge": "lllyasviel/control_v11p_sd15_softedge", # HED/Softedge
    }
    
    def __init__(self, device: str):
        self.device = device
        self.torch_dtype = torch.float16 if device == "cuda" else torch.float32
        self.cache = {} # In-memory cache of loaded controlnets

    def get_controlnet(self, control_type: str) -> Optional[Any]:
        """
        Get ControlNet model for specific type.
        """
        if not control_type:
            return None
            
        control_type = control_type.lower()
        
        # Mapping aliases
        if control_type == "openpose": control_type = "pose"
        if control_type == "sketch": control_type = "scribble"
        if control_type == "segmentation": control_type = "seg"
        if control_type == "edges": control_type = "canny"
        
        if control_type not in self.MODEL_MAP:
            logger.warning(f"ControlNet type '{control_type}' not supported. Ignoring.")
            return None
            
        model_id = self.MODEL_MAP[control_type]
        
        if model_id in self.cache:
            return self.cache[model_id]
            
        logger.info(f"Loading ControlNet: {model_id}")
        
        if not DIFFUSERS_AVAILABLE:
            logger.warning("Diffusers not available. Returning Mock ControlNet.")
            return "MOCK_CONTROLNET"
            
        # Check cache again just in case (race condition logic if async, but here sync)
        
        try:
             # Lazy loading
             controlnet = ControlNetModel.from_pretrained(
                 model_id, 
                 torch_dtype=self.torch_dtype
             )
             
             # Don't move to device immediately if using enable_model_cpu_offload in SD pipeline
             # But usually pipeline.to(device) or similar handles it.
             # If using `enable_model_cpu_offload`, strictly keep it on CPU for now?
             # For now, let's keep it in CPU RAM until pipeline attachment
             
             self.cache[model_id] = controlnet
             return controlnet
             
        except Exception as e:
            logger.error(f"Failed to load ControlNet {model_id}: {e}")
            return None
