//
//  PresetCardView.swift
//  FixPix
//

import SwiftUI

struct Preset: Identifiable {
    let id: String
    let name: String
    let thumbnailName: String // System image for mock
}

struct PresetCardView: View {
    let preset: Preset
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 10) {
                // Thumbnail Placeholder
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(FixPixColors.secondaryBackground)
                        .frame(width: 80, height: 80)
                    
                    Image(systemName: preset.thumbnailName)
                        .font(.title2)
                        .foregroundColor(isSelected ? FixPixColors.fixpixBrandBlue : FixPixColors.secondaryText)
                    
                    if isSelected {
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(FixPixColors.fixpixBrandBlue, lineWidth: 2)
                    }
                }
                
                Text(preset.name)
                    .font(FixPixFonts.caption)
                    .foregroundColor(isSelected ? FixPixColors.fixpixBrandBlue : FixPixColors.text)
                    .fontWeight(isSelected ? .bold : .regular)
            }
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    ZStack {
        Color.black.ignoresSafeArea()
        HStack(spacing: 16) {
            PresetCardView(preset: Preset(id: "1", name: "Natural", thumbnailName: "leaf.fill"), isSelected: true, action: {})
            PresetCardView(preset: Preset(id: "2", name: "Vivid", thumbnailName: "sun.max.fill"), isSelected: false, action: {})
        }
        .padding()
    }
}
