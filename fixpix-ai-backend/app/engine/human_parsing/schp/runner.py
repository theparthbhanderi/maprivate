import cv2
import numpy as np
import logging
from app.engine.human_parsing.base import HumanParsingModel
from app.engine.human_parsing.factory import HumanParsingFactory

logger = logging.getLogger(__name__)

# LIP Dataset Labels (20 classes)
LIP_LABELS = {
    0: 'Background', 1: 'Hat', 2: 'Hair', 3: 'Glove', 
    4: 'Sunglasses', 5: 'UpperClothes', 6: 'Dress', 7: 'Coat',
    8: 'Socks', 9: 'Pants', 10: 'Jumpsuits', 11: 'Scarf', 
    12: 'Skirt', 13: 'Face', 14: 'Left-arm', 15: 'Right-arm', 
    16: 'Left-leg', 17: 'Right-leg', 18: 'Left-shoe', 19: 'Right-shoe'
}

class SCHPRunner(HumanParsingModel):
    def load(self) -> None:
        logger.info("Loading SCHP (Self-Correction for Human Parsing)...")
        # Real implementation would load ResNet101 based SCHP model
        # For now, we use a robust Mock that generates a dummy parsing map
        # based on simple heuristics (Top=Head, Middle=Body, Bottom=Legs)
        self.model = "MOCK_SCHP"
        self.is_loaded = True

    def predict(self, img: np.ndarray, **kwargs) -> tuple:
        if not self.is_loaded:
            self.load()
            
        logger.info("Running SCHP Parsing...")
        h, w = img.shape[:2]
        
        # Mock Semantic Map Generation
        # 0=BG, 2=Hair, 13=Face, 5=UpperClothes, 9=Pants
        parsing_map = np.zeros((h, w), dtype=np.uint8)
        
        # Simple Geometric Mocking for Demo
        # Center x
        cx = w // 2
        
        # 1. Hair (Top ellipse)
        cv2.ellipse(parsing_map, (cx, int(0.15*h)), (int(0.15*w), int(0.1*h)), 0, 0, 360, 2, -1)
        
        # 2. Face (Below hair)
        cv2.ellipse(parsing_map, (cx, int(0.25*h)), (int(0.12*w), int(0.12*h)), 0, 0, 360, 13, -1)
        
        # 3. Upper Clothes (Torso)
        cv2.rectangle(parsing_map, (cx - int(0.25*w), int(0.4*h)), (cx + int(0.25*w), int(0.7*h)), 5, -1)
        
        # 4. Arms (simple rects)
        cv2.rectangle(parsing_map, (cx - int(0.35*w), int(0.4*h)), (cx - int(0.25*w), int(0.7*h)), 14, -1)
        cv2.rectangle(parsing_map, (cx + int(0.25*w), int(0.4*h)), (cx + int(0.35*w), int(0.7*h)), 15, -1)
        
        # 5. Pants (Bottom)
        cv2.rectangle(parsing_map, (cx - int(0.20*w), int(0.7*h)), (cx - int(0.05*w), int(0.95*h)), 9, -1) # Left leg area
        cv2.rectangle(parsing_map, (cx + int(0.05*w), int(0.7*h)), (cx + int(0.20*w), int(0.95*h)), 9, -1) # Right leg area
        
        # Blur to make it look "organic" / neural network-like output
        # But we need integer IDs, so nearest neighbor or safe blur?
        # Actually neural nets produce sharp edges or we argmax. 
        # Making it blocky is safer for mock.
        
        return parsing_map, LIP_LABELS

HumanParsingFactory.register("schp", SCHPRunner)
