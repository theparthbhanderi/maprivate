from typing import Dict, Type
from app.engine.colorizer.base import ColorizerModel
from app.engine.device import get_device

class ColorizerFactory:
    _registry: Dict[str, Type[ColorizerModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[ColorizerModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> ColorizerModel:
        if name not in cls._registry:
            raise ValueError(f"Colorizer '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)

# We will register models in their respective runner files
