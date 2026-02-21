# FixPix Demo & Evaluation Guide

## 🎓 College Viva / Evaluation Checklist

### 1. Preparation
- [ ] **Reset Environment**: Ensure you are in **Demo Mode**.
- [ ] **Clean Data**: If showing "First Run", uninstall and reinstall the app.
- [ ] **Preload Images**: Have high-res photos ready in your Camera Roll (Portraits, Landscapes).

### 2. The Golden Flow (Demo Script)
1.  **Launch**: Open App -> Show **Launch Animation** -> Land on **Home**.
    *   *Talk point: "Premium entry experience with branded motion."*
2.  **Upload**: Tap "+" -> Select Photo -> Show **Upload Analysis**.
    *   *Talk point: "Smart image analysis before editing."*
3.  **Editor**: Land on Editor -> Zoom in/out -> Pan.
    *   *Talk point: "Metal-performant canvas with 60fps interaction."*
4.  **Action**: Select **Magic Wand** -> Tap **Apply**.
    *   *Talk point: "Asynchronous AI processing pipeline."*
5.  **Processing**: Show **Glass Overlay** -> Progress Bar -> Success Haptic.
6.  **Result**: View Result -> **Export** -> Save to Photos.
    *   *Talk point: "Production-grade export flow."*
7.  **Profile**: Go to Profile -> Show **History** -> Tap Project to re-open.
    *   *Talk point: "Data persistence and session management."*

### 3. Technical Q&A Cheat Sheet
- **Architecture**: MVVM + Clean Architecture (Services, Models, Views).
- **Concurrency**: Swift Concurrency (`async/await`) + Actors for thread safety.
- **UI System**: 100% SwiftUI, custom Design System (`FixPixTheme`).
- **Backend Readiness**: Abstracted `APIClient` protocol ready for Python/FastAPI integration.
- **Performance**: LazyVGrid for lists, minimized main-thread work.

## 🛠 Debugging & Control
- **Logs**: filtered by `[FixPix]` in Xcode console.
- **Demo Reset**: Toggling "Environment" in code resets the mock session.
