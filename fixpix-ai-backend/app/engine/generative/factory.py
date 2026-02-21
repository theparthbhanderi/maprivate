from typing import Dict, Type
from app.engine.generative.base import GenerativeModel
from app.engine.device import get_device

class GenerativeFactory:
    _registry: Dict[str, Type[GenerativeModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[GenerativeModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> GenerativeModel:
        if name not in cls._registry:
            raise ValueError(f"Generative model '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)
