# FixPix API Contract

## Base URL
- **Web**: `http://localhost:8000`
- **Android**: `http://10.0.2.2:8000` (Emulator)

## Authentication (JWT)
### Login
- **Endpoint**: `POST /api/token/`
- **Body**: `{ "username": "...", "password": "..." }`
- **Response**: `{ "access": "...", "refresh": "..." }`

### Register
- **Endpoint**: `POST /api/register/`
- **Body**: `{ "username": "...", "email": "...", "password": "..." }`

## Projects (Images)
### Upload Image
- **Endpoint**: `POST /api/images/`
- **Header**: `Authorization: Bearer <token>`
- **Type**: `multipart/form-data`
- **Body**: `original_image` (File)

### Get History
- **Endpoint**: `GET /api/images/`
- **Response**: List of Project Objects.

## AI Processing
### Trigger Processing
- **Endpoint**: `POST /api/images/{id}/process_image/`
- **Body (JSON)**:
  ```json
  {
    "settings": {
      "upscaleX": 2,
      "faceRestoration": true,
      "removeScratches": false,
      "brightness": 1.1
    },
    "mask": "base64_string..." // Optional (for Object Eraser)
  }
  ```

### Poll Status
- **Endpoint**: `GET /api/images/{id}/`
- **Response**:
  ```json
  {
    "id": "uuid",
    "status": "processing", // pending, processing, completed, failed
    "processed_image": "url/to/image.jpg"
  }
  ```

## Reference
Refer to `/backend/api/` source code for exact serializer definitions.
