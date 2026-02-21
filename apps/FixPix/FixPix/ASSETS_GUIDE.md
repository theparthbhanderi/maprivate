# FixPix Asset Preparation Guide

## 1. App Icon
The AppIcon needs to be generated in multiple sizes for different devices.
1. Use a tool like **Icon Set Creator** or **Figma** to export your logo.
2. Locate the file `FixPix/Assets.xcassets/AppIcon.appiconset/Contents.json`.
3. You need to populate this folder with PNGs of the following sizes:
   - 20pt (1x, 2x, 3x)
   - 29pt (1x, 2x, 3x)
   - 40pt (1x, 2x, 3x)
   - 60pt (1x, 2x, 3x) - **Main iPhone Icon**
   - 1024pt (1x) - **App Store Icon**

*Tip: The icon should match the style of `FixPixLogo.swift` - a blue gradient rounded square with a white wand icon.*

## 2. Launch Screen
While `LaunchView.swift` handles the animated introduction, iOS requires a static Launch Screen for the split second before the app code loads.

1. Open Xcode.
2. Navigate to `LaunchScreen.storyboard` (if it exists) or creating one.
3. Set the `View` background color to **RGB(10, 10, 10)** (matching `FixPixColors.fixpixDarkBg`).
4. Add a `System Image` View: `wand.and.stars.inverse`.
5. Center it in the container.
6. Set Tint Color to `System Blue` or White.

This ensures a seamless transition from tap -> static launch -> animated launch.

## 3. Metadata (Info.plist)
Ensure the following keys are set in your target settings:
- **Display Name**: FixPix
- **Version**: 1.0.0
- **Build**: 100
- **Supported Interface Orientations**: Portrait (Locking to portrait is recommended for V1 of a photo editor).

## 4. Final Polish Checklist
- [x] Check Dark Mode contrast (App defaults to Dark Mode).
- [ ] Verify Tap areas are > 44pt (Check `FixPixButton` padding).
- [ ] Ensure all text is legible against the dark background.
