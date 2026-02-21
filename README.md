# FixPix — Mobile & Web AI Editor

A professional, full-stack AI photo restoration platform featuring a modern React frontend, native Android application, and a shared Django AI backend.

## 📂 Repository Structure

| Path | Description |
| :--- | :--- |
| **`apps/android`** | **Native Android App** (Kotlin, Jetpack Compose, Hilt, Retrofit). Open via Android Studio. |
| **`apps/website`** | **Web Application** (React, Vite, TailwindCSS). The production-grade editor interface. |
| **`backend`** | **Unified API Service** (Django REST Framework). Powers both Android and Web clients. |
| **`infra`** | **Infrastructure** (Docker, environment configs). |
| **`scripts`** | **Automation** (One-click startup scripts). |
| **`docs`** | **Documentation** (Architecture, Setup Guides, API Specs). |

## 🚀 Quick Start

### 1. Prerequisites
- Python 3.10+
- Node.js 18+
- Android Studio (for mobile dev)

### 2. Run Everything
Start the Backend and Website simultaneously:
```bash
./scripts/run_all.sh
```
- **Web**: [http://localhost:5173](http://localhost:5173)
- **API**: [http://localhost:8000](http://localhost:8000)

### 3. Native Android Development
Refer to [docs/android_setup.md](docs/android_setup.md) for detailed instructions.
**tl;dr**: Open `apps/android` in Android Studio and run the `app` configuration on an emulator.

## 🛠 Tech Stack
- **Mobile**: Android (Kotlin), Jetpack Compose, Coroutines, Coil, Retrofit.
- **Web**: React, Vite, Framer Motion, TailwindCSS.
- **Backend**: Django, DRF, OpenCV, PyTorch (AI Logic).
- **DevOps**: Monorepo orchestration, Docker-ready structure.

## 📖 Documentation
- [System Architecture](docs/architecture.md)
- [API Contract](docs/api_contract.md)
