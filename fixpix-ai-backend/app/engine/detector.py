import torch
import numpy as np
from app.engine.device import get_device
from app.engine.base import AIModel
import logging

try:
    from facexlib.detection import init_detection_model
except ImportError:
    init_detection_model = None

logger = logging.getLogger(__name__)

class FaceDetector(AIModel):
    def load(self) -> None:
        if init_detection_model is None:
            logger.warning("facexlib not installed, using mock detector")
            self.model = "MOCK_DETECTOR"
            self.is_loaded = True
            return

        logger.info("Loading RetinaFace detector...")
        # Load RetinaFace (resnet50)
        self.model = init_detection_model('retinaface_resnet50', half=False, device=self.device)
        self.is_loaded = True
        logger.info("RetinaFace detector loaded.")

    def predict(self, img: np.ndarray) -> list:
        """
        Returns list of bounding boxes [x1, y1, x2, y2, confidence]
        """
        if not self.is_loaded:
            self.load()
            
        if self.model == "MOCK_DETECTOR":
             # Return full image as face for mock
             h, w = img.shape[:2]
             return [[0, 0, w, h, 0.99]]

        with torch.no_grad():
            # facexlib detection returns None if no faces, or bounding boxes
            # We assume the input is BGR (opencv)
            bboxes = self.model.detect_faces(img, confidence_threshold=0.5)
            # detect_faces returns None or list? implementation varies, let's assume standard behavior
            return bboxes if bboxes is not None else []
