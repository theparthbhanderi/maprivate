import os
from pydantic_settings import BaseSettings
from functools import lru_cache
from typing import List

class Settings(BaseSettings):
    PROJECT_NAME: str = "FixPix AI Backend"
    VERSION: str = "0.1.0"
    API_V1_STR: str = "/api/v1"
    
    # CORS
    BACKEND_CORS_ORIGINS: List[str] = ["http://localhost:5173", "http://localhost:3000", "*"]
    
    # AI Settings
    MAX_IMAGE_SIZE_MB: int = 10
    MODEL_CACHE_DIR: str = os.path.join(os.getcwd(), "models")
    
    # external services
    REPLICATE_API_TOKEN: str = ""
    
    # Google Drive File IDs (Public Shareable Links)
    # TODO: User needs to populate these with actual IDs
    MODEL_LINKS: dict = {
        "gfpgan": "1-placeholder-id-for-gfpgan-v1.4", 
        "codeformer": "1-placeholder-id-for-codeformer",
        "realesrgan": "1-placeholder-id-for-realesrgan",
    }
    
    # Hardware
    FORCE_CPU: bool = False
    
    class Config:
        env_file = ".env"

@lru_cache()
def get_settings():
    return Settings()

settings = get_settings()
