//
//  ToolPanelView.swift
//  FixPix
//
//

import SwiftUI

struct ToolPanelView: View {
    @ObservedObject var viewModel: ToolPanelViewModel
    let onClose: () -> Void
    let onApply: () -> Void
    
    // Mock Presets
    let mockPresets = [
        Preset(id: "1", name: "Natural", thumbnailName: "leaf.fill"),
        Preset(id: "2", name: "Vivid", thumbnailName: "sun.max.fill"),
        Preset(id: "3", name: "Studio", thumbnailName: "camera.filter"),
        Preset(id: "4", name: "Dramatic", thumbnailName: "theatermasks.fill"),
        Preset(id: "5", name: "Soft", thumbnailName: "cloud.fill")
    ]
    
    var body: some View {
        VStack(spacing: 0) {
            // Drag Indicator
            Capsule()
                .fill(Color.gray.opacity(0.3))
                .frame(width: 40, height: 4)
                .padding(.top, 12)
            
            // Header
            ToolHeaderView(
                title: viewModel.tool.rawValue,
                iconName: iconForTool(viewModel.tool),
                onReset: { viewModel.resetToDefaults() },
                onClose: onClose
            )
            
            ScrollView(showsIndicators: false) {
                VStack(spacing: 32) {
                    // Standard Intensity Slider
                    FixPixSlider(label: "Intensity", value: $viewModel.intensity)
                    
                    // Tool Specific Controls
                    if isAdjustmentTool(viewModel.tool) {
                        VStack(spacing: 24) {
                            FixPixSlider(label: "Brightness", value: $viewModel.brightness, range: -1...1)
                            FixPixSlider(label: "Contrast", value: $viewModel.contrast, range: -1...1)
                        }
                    } else if isFilterTool(viewModel.tool) {
                        // Presets Grid
                        VStack(alignment: .leading, spacing: 16) {
                            Text("Styles")
                                .font(FixPixFonts.button())
                            
                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack(spacing: 16) {
                                    ForEach(mockPresets) { preset in
                                        PresetCardView(
                                            preset: preset,
                                            isSelected: viewModel.selectedPresetId == preset.id,
                                            action: { 
                                                withAnimation(AnimationUtils.snappiest) {
                                                    viewModel.selectedPresetId = preset.id
                                                }
                                                HapticManager.shared.selection()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Final CTA
                    FixPixButton(title: "Apply \(viewModel.tool.rawValue)", style: .primary) {
                        viewModel.applyChanges()
                        onApply()
                    }
                    .padding(.top, 8)
                }
                .padding(.horizontal, 24)
                .padding(.bottom, 40)
            }
        }
        .frame(maxWidth: .infinity)
        .background(FixPixColors.background)
        .fixpixGlassPanel() // Premium glass effect
        .cornerRadius(32, corners: [.topLeft, .topRight])
        .shadow(color: Color.black.opacity(0.3), radius: 30, x: 0, y: -10)
    }
    
    private func iconForTool(_ tool: ToolType) -> String {
        switch tool {
        case .enhance: return "wand.and.stars"
        case .restore: return "person.fill.viewfinder"
        case .scratchRemoval: return "bandage.fill"
        case .colorization: return "paintpalette.fill"
        case .upscale: return "arrow.up.left.and.arrow.down.right.circle.fill"
        case .brightness: return "sun.max.fill"
        case .contrast: return "circle.lefthalf.filled"
        case .saturation: return "drop.fill"
        }
    }
    
    private func isAdjustmentTool(_ tool: ToolType) -> Bool {
        return [.brightness, .contrast, .saturation].contains(tool)
    }
    
    private func isFilterTool(_ tool: ToolType) -> Bool {
        return [.enhance, .restore, .colorization].contains(tool)
    }
}

// Extension to support selective corner radius
extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners
    
    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

#Preview {
    ZStack(alignment: .bottom) {
        Color.black.ignoresSafeArea()
        ToolPanelView(viewModel: ToolPanelViewModel(tool: .enhance), onClose: {}, onApply: {})
    }
}
