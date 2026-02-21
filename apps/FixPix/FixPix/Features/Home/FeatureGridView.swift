//
//  FeatureCardView.swift
//  FixPix
//

import SwiftUI

struct Feature {
    let title: String
    let description: String
    let iconName: String
    let color: Color
}

struct FeatureCardView: View {
    let feature: Feature
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Icon
            ZStack {
                Circle()
                    .fill(feature.color.opacity(0.1))
                    .frame(width: 48, height: 48)
                
                Image(systemName: feature.iconName)
                    .font(.system(size: 20, weight: .semibold))
                    .foregroundColor(feature.color)
            }
            
            // Text
            VStack(alignment: .leading, spacing: 4) {
                Text(feature.title)
                    .font(FixPixFonts.button())
                    .foregroundColor(FixPixColors.text)
                
                Text(feature.description)
                    .font(FixPixFonts.caption)
                    .foregroundColor(FixPixColors.secondaryText)
                    .lineLimit(2)
            }
        }
        .padding(FixPixSpacing.cardPadding)
        .frame(maxWidth: .infinity, alignment: .leading)
        .fixpixCard()
    }
}

struct FeatureGridView: View {
    let features: [Feature] = [
        Feature(title: "AI Enhance", description: "Auto-fix lighting and details", iconName: "wand.and.stars", color: .blue),
        Feature(title: "Face Restore", description: "Bring back facial details", iconName: "person.fill.viewfinder", color: .purple),
        Feature(title: "Scratch Removal", description: "Fix cracks and damage", iconName: "bandage.fill", color: .orange),
        Feature(title: "Colorize", description: "Revive black & white photos", iconName: "paintpalette.fill", color: .pink)
    ]
    
    let columns = [
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16)
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            Text("Powerful AI Tools")
                .font(FixPixFonts.sectionHeader)
                .padding(.horizontal, FixPixSpacing.horizontal)
            
            LazyVGrid(columns: columns, spacing: 16) {
                ForEach(features, id: \.title) { feature in
                    FeatureCardView(feature: feature)
                }
            }
            .padding(.horizontal, FixPixSpacing.horizontal)
        }
    }
}

#Preview {
    ZStack {
        Color.black.edgesIgnoringSafeArea(.all)
        FeatureGridView()
    }
}
