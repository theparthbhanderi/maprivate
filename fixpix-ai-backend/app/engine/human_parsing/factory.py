from typing import Dict, Type
from app.engine.human_parsing.base import HumanParsingModel
from app.engine.device import get_device

class HumanParsingFactory:
    _registry: Dict[str, Type[HumanParsingModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[HumanParsingModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> HumanParsingModel:
        if name not in cls._registry:
            raise ValueError(f"Human Parsing model '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)
