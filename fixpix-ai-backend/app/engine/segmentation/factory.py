from typing import Dict, Type
from app.engine.segmentation.base import SegmentationModel
from app.engine.device import get_device

class SegmentationFactory:
    _registry: Dict[str, Type[SegmentationModel]] = {}

    @classmethod
    def register(cls, name: str, model_class: Type[SegmentationModel]):
        cls._registry[name] = model_class

    @classmethod
    def create(cls, name: str) -> SegmentationModel:
        if name not in cls._registry:
            raise ValueError(f"Segmentation model '{name}' not found. Available: {list(cls._registry.keys())}")
        
        device = get_device()
        return cls._registry[name](device)
