import cv2
import numpy as np
import base64
from app.engine.loader import model_manager
from app.engine.controller import hybrid_controller
import logging

logger = logging.getLogger(__name__)

class ImageService:
    @staticmethod
    def decode_image(file_bytes: bytes) -> np.ndarray:
        """Decode bytes to OpenCV image."""
        nparr = np.frombuffer(file_bytes, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
        return img

    @staticmethod
    def encode_image(img: np.ndarray, format: str = ".jpg") -> bytes:
        """Encode OpenCV image to bytes."""
        success, encoded_img = cv2.imencode(format, img)
        if not success:
            raise ValueError("Could not encode image")
        return encoded_img.tobytes()

    @staticmethod
    def restore_face(image_data: bytes, mode: str = "fast", fidelity: float = 0.5) -> dict:
        """
        Main business logic for face restoration.
        mode: 'fast' (GFPGAN) or 'quality' (CodeFormer)
        """
        img = ImageService.decode_image(image_data)
        
        if mode == "quality":
            # Fidelity 0.7 is a good balance for CodeFormer
            result_img, face_count = hybrid_controller.restore(img, mode="quality", fidelity=fidelity)
        else:
            result_img, face_count = hybrid_controller.restore(img, mode="fast")
            
        encoded_bytes = ImageService.encode_image(result_img)
        
    @staticmethod
    def upscale_image(image_data: bytes, scale: int = 4, enhance_faces: bool = False, fast_mode: bool = False) -> dict:
        """
        Business logic for Super Resolution.
        """
    @staticmethod
    def _smart_resize(img: np.ndarray, max_dim: int = 2048) -> np.ndarray:
        """
        Downscale image if larger than max_dim to prevent OOM.
        """
        h, w = img.shape[:2]
        if max(h, w) > max_dim:
            logger.info(f"Smart Resizing: Input {w}x{h} -> Max {max_dim}")
            scale = max_dim / max(h, w)
            new_w = int(w * scale)
            new_h = int(h * scale)
            img = cv2.resize(img, (new_w, new_h), interpolation=cv2.INTER_AREA)
        return img

    @staticmethod
    def upscale_image(image_data: bytes, scale: int = 4, enhance_faces: bool = False, fast_mode: bool = False) -> dict:
        """
        Business logic for Super Resolution.
        """
        img = ImageService.decode_image(image_data)
        
        # Smart Resize Optimization
        # If image is > 2048px, resizing to 4x will be 8K+ (Huge VRAM usage).
        # We cap input at 2048px.
        img = ImageService._smart_resize(img, max_dim=2048)
        
        h, w = img.shape[:2]
        orig_res = f"{w}x{h}"
        
        # Use Controller
        from app.engine.upscaler.sr_controller import sr_controller
        
        # Predict
        result_img = sr_controller.upscale(img, scale=scale, enhance_faces=enhance_faces, fast_mode=fast_mode)
        
        h_new, w_new = result_img.shape[:2]
        new_res = f"{w_new}x{h_new}"
        
        encoded_bytes = ImageService.encode_image(result_img, format=".png") # PNG for quality
        
        return {
            "image": encoded_bytes,
            "scale": scale,
            "original_resolution": orig_res,
            "new_resolution": new_res,
            "mode": "realesrgan" + ("+face" if enhance_faces else "")
        }

    @staticmethod
    def inpaint_image(image_data: bytes, mask_data: bytes, mode: str = "remove", prompt: str = "") -> dict:
        """
        Business logic for Inpainting.
        mode: 'remove' (LaMa) or 'generate' (Stable Diffusion - future)
        """
        img = ImageService.decode_image(image_data)
        mask_img = ImageService.decode_image(mask_data)
        
        # Ensure mask is single channel (grayscale)
        if len(mask_img.shape) == 3:
            mask_img = cv2.cvtColor(mask_img, cv2.COLOR_BGR2GRAY)
            
        # Optional: Smart Resize for Inpainting too if needed, but strict pixel alignment with mask is critical.
        # So we skip resizing unless explicit OOM protection is needed.
        
    @staticmethod
    def inpaint_image(image_data: bytes, mask_data: bytes, mode: str = "fast", prompt: str = "", strength: float = 0.8, feathering: int = 9) -> dict:
        """
        Business logic for Inpainting.
        mode: 'fast' (LaMa) or 'smart' (Stable Diffusion)
        """
        img = ImageService.decode_image(image_data)
        mask_img = ImageService.decode_image(mask_data)
        
        # Ensure mask is single channel (grayscale)
        if len(mask_img.shape) == 3:
            mask_img = cv2.cvtColor(mask_img, cv2.COLOR_BGR2GRAY)
            
        # Use Controller
        from app.engine.inpainter.controller import inpaint_controller
        from app.engine.device import get_device
        
        # Map modes
        controller_mode = "fast"
        # API "remove" -> "fast", "generate" -> "smart"
        if mode in ["smart", "generate"]:
            controller_mode = "smart"
            
        result_img = inpaint_controller.inpaint(img, mask_img, mode=controller_mode, prompt=prompt, strength=strength, feathering=feathering)
        
        encoded_bytes = ImageService.encode_image(result_img, format=".png")
        
        return {
            "image": encoded_bytes,
            "mode": controller_mode,
            "engine": "lama" if controller_mode == "fast" else "stable-diffusion",
            "device": get_device().type 
        }

    @staticmethod
    def colorize_image(image_data: bytes, mode: str = "artistic", render_factor: int = 35, enhance_faces: bool = False) -> dict:
        """
        Colorize B&W Image.
        """
        img = ImageService.decode_image(image_data)
        
        # Check B&W? DeOldify handles it, but we can log.
        if len(img.shape) == 3:
             # Check saturation
             hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
             s_mean = hsv[:,:,1].mean()
             if s_mean > 30: # Some color exists
                 logger.info(f"Image has color (S={s_mean}), but running colorization anyway.")

        model = model_manager.get_model("colorizer")
        
        # predict now accepts mode to switch weights if needed
        result_img = model.predict(img, mode=mode, render_factor=render_factor, enhance_faces=enhance_faces)
        
        encoded_bytes = ImageService.encode_image(result_img, format=".png")
        from app.engine.device import get_device
        
            "mode": mode,
            "device": get_device().type
        }

    @staticmethod
    def segment_image(image_data: bytes, mode: str = "auto", prompts: str = None, feather: int = 0, return_type: str = "cutout") -> dict:
        """
        Segment Image (Remove Background).
        mode: 'auto' (RMBG) or 'interactive' | 'refine' (SAM)
        prompts: JSON string (points/box)
        feather: Blur radius (0-40)
        return_type: 'cutout' | 'mask' | 'transparent' (same as cutout) | 'all'
        """
        img = ImageService.decode_image(image_data)
        
        # Use Controller
        from app.engine.segmentation.controller import segmentation_controller
        
        # Predict (Returns RGBA image)
        result_rgba = segmentation_controller.segment(img, mode=mode, prompts=prompts, feather=feather)
        
        b, g, r, a = cv2.split(result_rgba)
        
        # Prepare response components
        response = {
            "mode": mode,
            "device": get_device().type,
            "feather": feather
        }
        
        # Helper to encode
        def to_b64(cv_img, fmt=".png"):
             return ImageService.encode_image(cv_img, format=fmt)

        # Always return the main requested type
        # 'cutout' / 'transparent' -> The RGBA image
        if return_type in ["cutout", "transparent", "all"]:
            response["image"] = to_b64(result_rgba) # The cutout
            
        # 'mask' -> The B&W Mask
        if return_type in ["mask", "all"]:
            response["mask"] = to_b64(a)
            
        # Preview separation? 
        # API requires: cutout image, alpha mask, preview image
        # Let's return all if they modify response model to support distinct fields
        # Ideally we follow the signature requested.
        
        # If the API endpoint expects specific fields, we populate them.
        # Let's populate 'cutout_image' and 'mask_image'.
        # Legacy 'image' field for backward compat or primary result.
        
        response["cutout_image"] = to_b64(result_rgba)
        response["mask_image"] = to_b64(a)
        
        # Preview? Maybe a small jpg?
        # User asked for "preview image". Usually small size.
        small_preview = cv2.resize(result_rgba, (0,0), fx=0.25, fy=0.25)
        response["preview_image"] = to_b64(small_preview)
        
        # If legacy field needed
        response["image"] = response["cutout_image"]

        return response

image_service = ImageService()
