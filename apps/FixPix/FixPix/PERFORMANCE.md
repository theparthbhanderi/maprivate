# FixPix Performance Checklist

## Rendering
- [x] Use `resizable()` on all Images to avoid full-size buffer loading.
- [x] Use `LazyVGrid` for project lists to support large history.
- [x] Avoid `BlurView` on large areas during interactions (used mainly in overlays).
- [ ] Implement `drawingGroup()` for complex composed layers if frame rate drops.

## Memory
- [x] `EditorViewModel` manages `UIImage` lifecycle carefully.
- [x] Mock services utilize stateless functions where possible.
- [ ] In production, ensure high-res images are downsampled for `ImageCanvasView` preview.
- [ ] Use `autoreleasepool` block around heavy CoreImage processing steps.

## Concurrency
- [x] Use `Task` and `async/await` for all heavy operations.
- [x] Main thread is reserved for UI updates (via `@MainActor`).
- [x] Processing tasks are cancellable via `Task.handle`.

## Startup
- [x] `LaunchView` masks initial resource loading.
- [x] `ThemeManager` loads preferences from `UserDefaults` (fast).
- [ ] Defer heavy creation of `CIContext` until Editor opens.
