import logging
import replicate
import structlog
from app.core.config import settings

logger = logging.getLogger(__name__)

class ReplicateService:
    def __init__(self):
        self.api_token = settings.REPLICATE_API_TOKEN
        
    def upscale_image(self, file_bytes: bytes, scale: int) -> bytes:
        """
        Run Real-ESRGAN through Replicate API. 
        Returns the raw output image bytes.
        """
        try:
            import io
            # Replicate python client accepts file-like objects for images
            image_buffer = io.BytesIO(file_bytes)
            image_buffer.name = "input.jpg" # Required for the API to guess MIME type
            
            logger.info("Starting Replicate upscaling job...")
            
            output_url = replicate.run(
                "nightmareai/real-esrgan",
                input={
                    "image": image_buffer,
                    "scale": scale,
                    "face_enhance": False # Default off, adjust if needed
                }
            )
            
            # The output is a URL pointing to the resultant image
            if not output_url:
                 raise Exception("Replicate returned no URL")
                 
            logger.info(f"Replicate upscaling complete: {output_url}")
            
            # Fetch the actual image data from the URL
            import requests
            response = requests.get(output_url, timeout=30)
            response.raise_for_status()
            
            return response.content

        except Exception as e:
            logger.error(f"Replicate API failed: {str(e)}")
            raise e

replicate_service = ReplicateService()
