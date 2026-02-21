# FixPix Monorepo Architecture

## Overview
FixPix is a unified multi-platform AI photo editing ecosystem. The repository follows a monorepo structure to share backend services across Web, Android, and future iOS clients.

## Directory Structure
```
FixPix/
├── apps/
│   ├── android/        # Native Android (Kotlin/Jetpack Compose)
│   ├── website/        # Frontend (React/Vite/Tailwind)
│   └── ios/            # [Placeholder] Native iOS (SwiftUI)
│
├── backend/            # Shared Backend Service (Django REST Framework)
│   ├── api/            # REST Endpoints
│   ├── ai_engine/      # AI Image Processing Logic (OpenCV/PyTorch)
│
├── infra/              # Infrastructure Configuration (Docker, Nginx)
├── scripts/            # Orchestration Utilities (Start, Stop, Test)
└── docs/               # Technical Documentation
```

## Shared Backend Service
The backend acts as the single source of truth for:
- **Authentication**: JWT-based auth used by all clients.
- **Image Processing**: Heavy AI tasks (Restoration, Colorization) run asynchronously via Celery.
- **Asset Storage**: Manages original and processed images (Local/S3/Cloudinary).

## Network Flow
1. **Client (Web/Android)** uploads an image -> **Backend** (stores in DB/Storage).
2. **Client** requests processing -> **Backend** dispatches async task.
3. **Client** polls for status -> **Backend** returns status/result.
4. **Client** downloads result -> **Backend** serves optimized file.

## Data Flow
- **Auth**: `POST /api/token/` -> Access/Refresh Tokens.
- **Upload**: `multipart/form-data` to `/api/images/`.
- **Processing**: JSON payload to `/api/images/{id}/process_image/`.
