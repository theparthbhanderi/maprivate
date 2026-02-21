//
//  ToolButtonView.swift
//  FixPix
//

import SwiftUI

struct ToolButtonView: View {
    let tool: ToolType
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 8) {
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(isSelected ? FixPixColors.fixpixBrandBlue.opacity(0.15) : Color.clear)
                        .frame(width: 56, height: 56)
                    
                    Image(systemName: iconForTool(tool))
                        .font(.system(size: 20, weight: isSelected ? .bold : .medium))
                        .foregroundColor(isSelected ? FixPixColors.fixpixBrandBlue : FixPixColors.text)
                }
                
                Text(tool.rawValue)
                    .font(.system(size: 11, weight: isSelected ? .bold : .medium))
                    .foregroundColor(isSelected ? FixPixColors.text : FixPixColors.secondaryText)
            }
        }
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
}

//
//  ToolBarView.swift
//

import SwiftUI

struct ToolBarView: View {
    @ObservedObject var viewModel: EditorViewModel
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 24) {
                ForEach(ToolType.allCases, id: \.self) { tool in
                    ToolButtonView(
                        tool: tool,
                        isSelected: viewModel.selectedTool == tool,
                        action: {
                            withAnimation(.spring()) {
                                viewModel.selectedTool = tool
                            }
                        }
                    )
                }
            }
            .padding(.horizontal, 24)
            .padding(.vertical, 16)
        }
        .background(FixPixColors.background)
    }
}
