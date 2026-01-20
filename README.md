# FixPix

AI-powered image enhancement platform with professional-grade tools for upscaling, noise reduction, and intelligent restoration.

## 🏗️ Project Structure

```
/FixPix
├── website/          # React/Vite frontend
│   ├── src/          # Components, pages, hooks
│   ├── public/       # Static assets
│   └── package.json
│
├── backend/          # Django REST API + AI Engine
│   ├── api/          # REST endpoints, AI processing
│   ├── backend/      # Django settings
│   └── requirements.txt
│
├── docker-compose.yml
└── run_project.sh
```

## 🚀 Quick Start

### Prerequisites
- Node.js 18+
- Python 3.11+
- (Optional) Docker & Docker Compose

### Development Setup

**1. Start Backend:**
```bash
cd backend
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver
```

**2. Start Website:**
```bash
cd website
npm install
npm run dev
```

**3. Or use the convenience script:**
```bash
./run_project.sh  # macOS/Linux
run_project.bat   # Windows
```

### Docker Deployment
```bash
docker-compose up --build
```

## 🔌 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/register/` | POST | User registration |
| `/api/token/` | POST | JWT login |
| `/api/token/refresh/` | POST | Refresh JWT token |
| `/api/images/` | GET/POST | List/upload images |
| `/api/images/{id}/process_image/` | POST | AI enhancement |
| `/api/images/{id}/download/` | GET | Download result |

## 📱 Mobile Integration

Android and iOS apps can connect to the same backend API:

1. Set API base URL to your deployed backend
2. Use JWT authentication via `/api/token/`
3. All image processing endpoints are platform-agnostic

## 🔧 Environment Variables

See `.env.example` files in `website/` and `backend/` directories.

## 📄 License

MIT License
