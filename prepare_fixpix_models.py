#!/usr/bin/env python3
"""
FixPix AI Model Preparation Script
===================================
Downloads all required AI models for the FixPix backend.
Run this script ONCE on your local machine to prepare the models folder.
Then upload the entire `FixPix_AI_Models` folder to Google Drive.

Usage:
    python prepare_fixpix_models.py
"""

import os
import sys
import requests
from tqdm import tqdm

# ============================================================================
# Configuration: Model Registry
# ============================================================================
MODELS = [
    {
        "name": "GFPGAN",
        "filename": "GFPGANv1.4.pth",
        "folder": "gfpgan",
        "url": "https://github.com/TencentARC/GFPGAN/releases/download/v1.3.4/GFPGANv1.4.pth",
        "size_mb": 348,
    },
    {
        "name": "CodeFormer",
        "filename": "codeformer.pth",
        "folder": "codeformer",
        "url": "https://github.com/sczhou/CodeFormer/releases/download/v0.1.0/codeformer.pth",
        "size_mb": 377,
    },
    {
        "name": "Real-ESRGAN",
        "filename": "RealESRGAN_x4plus.pth",
        "folder": "realesrgan",
        "url": "https://github.com/xinntao/Real-ESRGAN/releases/download/v0.1.0/RealESRGAN_x4plus.pth",
        "size_mb": 64,
    },
]

# Root folder name
ROOT_FOLDER = "FixPix_AI_Models"

# ============================================================================
# Helpers
# ============================================================================

def create_folder_structure():
    """Create the root folder and subfolders."""
    print(f"\n📂 Creating folder structure: {ROOT_FOLDER}/")
    os.makedirs(ROOT_FOLDER, exist_ok=True)
    
    for model in MODELS:
        folder_path = os.path.join(ROOT_FOLDER, model["folder"])
        os.makedirs(folder_path, exist_ok=True)
        print(f"   ✅ Created: {folder_path}/")
    print()

def download_file(url: str, destination: str, expected_size_mb: int):
    """
    Download a file with progress bar.
    Supports large files via streaming.
    """
    # Check if file already exists
    if os.path.exists(destination):
        existing_size_mb = os.path.getsize(destination) / (1024 * 1024)
        # Allow 5% tolerance for size check
        if existing_size_mb >= expected_size_mb * 0.95:
            print(f"   ⏭️  File already exists ({existing_size_mb:.1f} MB). Skipping.")
            return True
        else:
            print(f"   ⚠️  Partial file detected ({existing_size_mb:.1f} MB). Re-downloading...")
            os.remove(destination)

    try:
        response = requests.get(url, stream=True, timeout=30)
        response.raise_for_status()
        
        total_size = int(response.headers.get('content-length', 0))
        
        with open(destination, 'wb') as f, tqdm(
            desc=f"   Downloading",
            total=total_size,
            unit='B',
            unit_scale=True,
            unit_divisor=1024,
        ) as bar:
            for chunk in response.iter_content(chunk_size=8192):
                if chunk:
                    f.write(chunk)
                    bar.update(len(chunk))
        
        print(f"   ✅ Download complete: {destination}")
        return True
        
    except requests.exceptions.RequestException as e:
        print(f"   ❌ Download failed: {e}")
        if os.path.exists(destination):
            os.remove(destination)
        return False

def download_all_models():
    """Download all models in the registry."""
    print("=" * 60)
    print("🚀 FixPix AI Model Downloader")
    print("=" * 60)
    
    create_folder_structure()
    
    success_count = 0
    
    for model in MODELS:
        print(f"📦 {model['name']} ({model['size_mb']} MB)")
        print(f"   Source: {model['url']}")
        
        destination = os.path.join(ROOT_FOLDER, model["folder"], model["filename"])
        
        if download_file(model["url"], destination, model["size_mb"]):
            success_count += 1
        print()
    
    print("=" * 60)
    if success_count == len(MODELS):
        print(f"✅ All {success_count} models downloaded successfully!")
        print(f"\n📤 Next Step: Upload the '{ROOT_FOLDER}' folder to Google Drive.")
    else:
        print(f"⚠️  Downloaded {success_count}/{len(MODELS)} models.")
        print("   Please re-run the script to retry failed downloads.")
    print("=" * 60)

# ============================================================================
# Entry Point
# ============================================================================

if __name__ == "__main__":
    # Check for required dependencies (already imported at top - this is just a fallback)
    download_all_models()

