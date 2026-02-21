import os
import requests
from tqdm import tqdm

MODEL_DIR = "models"
os.makedirs(MODEL_DIR, exist_ok=True)

MODELS = {
    "GFPGANv1.4.pth": "https://github.com/TencentARC/GFPGAN/releases/download/v1.3.0/GFPGANv1.4.pth",
    "parsing_parsenet.pth": "https://github.com/TencentARC/GFPGAN/releases/download/v1.3.0/parsing_parsenet.pth",
    "detection_Resnet50_Final.pth": "https://github.com/xinntao/facexlib/releases/download/v0.1.0/detection_Resnet50_Final.pth",
    "codeformer.pth": "https://github.com/sczhou/CodeFormer/releases/download/v0.1.0/codeformer.pth",
    "RealESRGAN_x4plus.pth": "https://github.com/xinntao/Real-ESRGAN/releases/download/v0.1.0/RealESRGAN_x4plus.pth"
}

def download_file(url, filename):
    local_path = os.path.join(MODEL_DIR, filename)
    if os.path.exists(local_path):
        print(f"✅ {filename} already exists.")
        return

    print(f"⬇️ Downloading {filename}...")
    response = requests.get(url, stream=True)
    total_size = int(response.headers.get('content-length', 0))
    
    with open(local_path, 'wb') as f, tqdm(
        desc=filename,
        total=total_size,
        unit='iB',
        unit_scale=True,
        unit_divisor=1024,
    ) as bar:
        for data in response.iter_content(chunk_size=1024):
            size = f.write(data)
            bar.update(size)

if __name__ == "__main__":
    print(f"🚀 Downloading models to {os.path.abspath(MODEL_DIR)}")
    for name, url in MODELS.items():
        try:
            download_file(url, name)
        except Exception as e:
            print(f"❌ Failed to download {name}: {e}")
            
    print("✨ All models downloaded!")
