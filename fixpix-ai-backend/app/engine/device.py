import torch
import logging
from app.core.config import settings

logger = logging.getLogger(__name__)

def get_device() -> torch.device:
    """
    Selects the best available device for inference.
    Order of preference: CUDA -> MPS (Mac) -> CPU
    """
    if settings.FORCE_CPU:
        logger.info("Device: CPU (Forced)")
        return torch.device("cpu")

    if torch.cuda.is_available():
        logger.info(f"Device: CUDA ({torch.cuda.get_device_name(0)})")
        return torch.device("cuda")
    
    if torch.backends.mps.is_available():
        logger.info("Device: MPS (Metal Performance Shaders)")
        return torch.device("mps")
    
    logger.info("Device: CPU")
    return torch.device("cpu")
