from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.config import settings
from app.core.logging import setup_logging
from app.api.v1.api import api_router

from app.engine.loader import model_manager
import logging

logger = logging.getLogger(__name__)

setup_logging()

app = FastAPI(
    title=settings.PROJECT_NAME,
    version=settings.VERSION,
    openapi_url=f"{settings.API_V1_STR}/openapi.json"
)

@app.on_event("startup")
async def startup_event():
    """Warming up AI models on startup."""
    logger.info("Warming up AI Engine...")
    # Pre-load detector and fast runner for immediate response
    try:
        model_manager.get_model("gfpgan") 
        # model_manager.get_model("face_detector") # If we exposed it in loader
        logger.info("Warmup complete.")
    except Exception as e:
        logger.error(f"Warmup failed: {e}")

# Set all CORS enabled origins
if settings.BACKEND_CORS_ORIGINS:
    app.add_middleware(
        CORSMiddleware,
        allow_origins=settings.BACKEND_CORS_ORIGINS,
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

app.include_router(api_router, prefix=settings.API_V1_STR)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
