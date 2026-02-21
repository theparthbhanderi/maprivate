from app.api.v1.endpoints import restoration, health, super_resolution, inpaint, colorize, segmentation, parsing, generative, assistant

api_router = APIRouter()
api_router.include_router(health.router, tags=["health"])
api_router.include_router(restoration.router, prefix="/images", tags=["images"])
api_router.include_router(upscale.router, prefix="/image", tags=["super-resolution"])
api_router.include_router(inpaint.router, prefix="/image", tags=["inpainting"])
api_router.include_router(colorize.router, prefix="/image", tags=["colorization"])
api_router.include_router(segmentation.router, prefix="/image", tags=["segmentation"])
api_router.include_router(parsing.router, prefix="/human", tags=["human-parsing"])
api_router.include_router(generative.router, prefix="/gen", tags=["generative"])
api_router.include_router(assistant.router, prefix="/assistant", tags=["assistant"])
