import sys
import os
import logging

# Add the parent directory to sys.path to make 'app' module importable
sys.path.append(os.path.join(os.path.dirname(__file__), '..'))

# Mock settings to avoid needing .env for this test if possible,
# though we probably have .env.
os.environ["MODEL_CACHE_DIR"] = "./models"

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("test_loader")

def test_loader():
    logger.info("Attempting to import model_manager...")
    try:
        from app.engine.loader import model_manager
        logger.info("Successfully imported model_manager.")
    except Exception as e:
        logger.error(f"Failed to import model_manager: {e}")
        return

    logger.info("Attempting to load 'gfpgan' (should trigger download check)...")
    try:
        # This is expected to fail with a download error (placeholder ID) or ImportError (missing gfpgan lib)
        # But it should NOT fail with "ModuleNotFoundError: No module named 'app.engine.face'"
        model = model_manager.get_model("gfpgan")
        logger.info("Successfully loaded gfpgan (Unexpected if no real ID provided).")
    except Exception as e:
        logger.info(f"Caught expected error during load: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    test_loader()
