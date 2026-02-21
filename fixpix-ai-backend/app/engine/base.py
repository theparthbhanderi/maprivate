from abc import ABC, abstractmethod
from typing import Any
import torch

class AIModel(ABC):
    """
    Abstract base class for all AI models in the system.
    Enforces a standard lifecycle: load -> predict -> unload.
    """
    
    def __init__(self, device: torch.device):
        self.device = device
        self.model: Any = None
        self.is_loaded: bool = False

    @abstractmethod
    def load(self) -> None:
        """Load model weights into memory/device."""
        pass

    @abstractmethod
    def predict(self, *args, **kwargs) -> Any:
        """Run inference."""
        pass

    def unload(self) -> None:
        """Unload model and free memory."""
        self.model = None
        self.is_loaded = False
        if self.device.type == 'cuda':
            torch.cuda.empty_cache()
        elif self.device.type == 'mps':
            torch.mps.empty_cache()
