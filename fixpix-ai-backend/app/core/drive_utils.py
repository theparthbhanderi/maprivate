import os
import requests
import logging
from app.core.config import settings

logger = logging.getLogger(__name__)

def download_file_from_google_drive(file_id: str, destination: str):
    """
    Downloads a file from Google Drive using its ID.
    Supports large files by stream-downloading chunks.
    """
    
    # Check if file already exists
    if os.path.exists(destination):
        logger.info(f"File {destination} already exists. Skipping download.")
        return

    logger.info(f"Downloading file from Google Drive (ID: {file_id}) to {destination}...")
    
    URL = "https://docs.google.com/uc?export=download"
    session = requests.Session()

    try:
        response = session.get(URL, params={'id': file_id}, stream=True)
        token = _get_confirm_token(response)

        if token:
            params = {'id': file_id, 'confirm': token}
            response = session.get(URL, params=params, stream=True)

        _save_response_content(response, destination)
        logger.info(f"Download complete: {destination}")

    except Exception as e:
        logger.error(f"Failed to download file from Drive: {e}")
        # Clean up partial file if it exists and failed
        if os.path.exists(destination):
            os.remove(destination)
        raise e

def _get_confirm_token(response):
    for key, value in response.cookies.items():
        if key.startswith('download_warning'):
            return value
    return None

def _save_response_content(response, destination):
    CHUNK_SIZE = 32768
    
    # Ensure directory exists
    os.makedirs(os.path.dirname(destination), exist_ok=True)

    with open(destination, "wb") as f:
        for chunk in response.iter_content(CHUNK_SIZE):
            if chunk: # filter out keep-alive new chunks
                f.write(chunk)

def ensure_model(model_name: str, filename: str):
    """
    Ensures a model file exists locally. If not, downloads it from the configuration links.
    """
    if model_name not in settings.MODEL_LINKS:
        logger.warning(f"No Google Drive link found for model '{model_name}'. Skipping download check.")
        return

    file_id = settings.MODEL_LINKS[model_name]
    destination = os.path.join(settings.MODEL_CACHE_DIR, filename)

    download_file_from_google_drive(file_id, destination)
    return destination
