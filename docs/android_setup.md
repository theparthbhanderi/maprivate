# Android Development Setup

## Prerequisites
- Android Studio Koala (or newer)
- JDK 17+
- Android SDK API 34+

## Opening the Project
**Do NOT** open the root `FixPix/` folder in Android Studio.

1. Launch **Android Studio**.
2. Select **Open**.
3. Navigate to `FixPix/apps/android`.
4. Click **Open**.

Gradle will sync automatically.

## Backend Connection
The Android app requires the local backend to be running.

### 1. Start Support Services
Run the full stack script from terminal:
```bash
./scripts/run_all.sh
```
Or start backend manually:
```bash
./scripts/run_backend.sh
```

### 2. Emulator Configuration
The Android app is configured to connect to `http://10.0.2.2:8000`. This is the special alias for the host machine's localhost from within the Android Emulator.
- **No config changes needed** if using the official Android Emulator.
- If using a physical device, ensure your phone and computer are on the same WiFi and update `BASE_URL` in `NetworkModule.kt` to your computer's local IP (e.g., `192.168.1.x`).

## Common Issues
- **"Connection Refused"**: Ensure the backend is running.
- **"Cleartext Traffic Not Permitted"**: The app allows cleartext traffic for localhost debugging. If connecting to a remote HTTP server, verify `network_security_config`.
