from typing import Dict, Type
from app.engine.inpainter.base import InpainterModel
from app.engine.device import get_device

class InpainterFactory:
    _registry: Dict[str, Type[InpainterModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[InpainterModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> InpainterModel:
        if name not in cls._registry:
            raise ValueError(f"Inpainter '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)

# We will register models in their respective runner files
