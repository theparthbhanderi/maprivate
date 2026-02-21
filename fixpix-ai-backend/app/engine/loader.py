from typing import Dict, Optional
import torch
from app.engine.device import get_device
from app.engine.base import AIModel
# Import actual runners (circular imports avoided by importing inside methods if needed)
# from app.engine.gfpgan.runner import GFPGANRunner
# from app.engine.codeformer.runner import CodeFormerRunner

class ModelManager:
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(ModelManager, cls).__new__(cls)
            cls._instance._initialized = False
        return cls._instance
    
    def __init__(self):
        if self._initialized:
            return
            
        self.device = get_device()
        self.models: Dict[str, AIModel] = {}
        self._initialized = True

    def get_model(self, model_name: str) -> Optional[Any]:
        if model_name in self.models:
            return self.models[model_name]
            
        logger.info(f"Loading model: {model_name}")
        
        # Load logic...
        if model_name == "retinaface":
            # from app.engine.detector import FaceDetector # Direct import if detector.py is in engine
            # Checking file structure, detector.py is directly in app/engine/
            from app.engine.detector import FaceDetector
            self.models[model_name] = FaceDetector(self.device).get_net()
        elif model_name == "gfpgan":
            from app.engine.gfpgan.runner import GFPGANRunner
            # from app.engine.face.factory import FaceEngineFactory # Factory pattern seems broken/missing based on file list
            # Instantiating runner directly as per file structure
            self.models[model_name] = GFPGANRunner(self.device)
            self.models[model_name].load()
        elif model_name == "codeformer":
            from app.engine.codeformer.runner import CodeFormerRunner
            self.models[model_name] = CodeFormerRunner(self.device)
            self.models[model_name].load()
        elif model_name == "realesrgan":
            from app.engine.super_resolution.realesrgan.runner import RealESRGANRunner
            from app.engine.super_resolution.factory import SuperResolutionFactory
            self.models[model_name] = SuperResolutionFactory.create("realesrgan")
        elif model_name == "inpainting_lama":
            from app.engine.inpainting.lama.runner import LaMaRunner
            from app.engine.inpainting.factory import InpaintingFactory
            self.models[model_name] = InpaintingFactory.create("lama")
        elif model_name == "inpainting_sd":
            from app.engine.inpainting.sd.runner import SDInpaintRunner
            from app.engine.inpainting.factory import InpaintingFactory
            self.models[model_name] = InpaintingFactory.create("stable_diffusion")
        elif model_name == "colorizer_deoldify":
            from app.engine.colorization.deoldify.runner import DeOldifyRunner
            from app.engine.colorization.factory import ColorizerFactory
            self.models[model_name] = ColorizerFactory.create("deoldify")
        elif model_name == "segmentation_rmbg":
            from app.engine.segmentation.rmbg.runner import RMBGRunner
            from app.engine.segmentation.factory import SegmentationFactory
            self.models[model_name] = SegmentationFactory.create("rmbg")
        elif model_name == "segmentation_sam":
            from app.engine.segmentation.sam.runner import SAMRunner
            from app.engine.segmentation.factory import SegmentationFactory
            self.models[model_name] = SegmentationFactory.create("sam")
        elif model_name == "human_parsing_schp":
            from app.engine.human_parsing.schp.runner import SCHPRunner
            from app.engine.human_parsing.factory import HumanParsingFactory
            self.models[model_name] = HumanParsingFactory.create("schp")
        elif model_name == "generative_sd":
            from app.engine.generative.sd.runner import SDRunner
            from app.engine.generative.factory import GenerativeFactory
            self.models[model_name] = GenerativeFactory.create("stable_diffusion")
        else:
            raise ValueError(f"Unknown model name: {model_name}")
            
        return self.models[model_name]

    def unload_all(self, exclude: list = None):
        """
        Aggressively unload models to free VRAM.
        """
        exclude = exclude or []
        keys_to_remove = []
        for name in self.models:
            if name not in exclude:
                # Force delete/cuda empty cache if needed
                logger.info(f"Unloading model: {name}")
                # If model has unload method, call it (some runners might)
                # But mostly we just remove reference and let GC + torch.cuda.empty_cache handle it
                keys_to_remove.append(name)
        
        for k in keys_to_remove:
            del self.models[k]
            
        gc.collect()
        if torch.cuda.is_available():
            torch.cuda.empty_cache()
            if hasattr(torch.cuda, 'ipc_collect'):
                torch.cuda.ipc_collect()
        logger.info(f"Unloaded {len(keys_to_remove)} models. VRAM cleared.")

model_manager = ModelManager()
