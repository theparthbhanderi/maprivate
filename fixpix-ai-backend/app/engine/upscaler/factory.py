from typing import Dict, Type
from app.engine.upscaler.base import UpscalerModel
from app.engine.device import get_device

class UpscalerFactory:
    _registry: Dict[str, Type[UpscalerModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[UpscalerModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> UpscalerModel:
        if name not in cls._registry:
            raise ValueError(f"Upscaler '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)

# We will register models in their respective runner files or a central registry init
