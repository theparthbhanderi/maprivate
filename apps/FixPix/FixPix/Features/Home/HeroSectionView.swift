//
//  HeroSectionView.swift
//  FixPix
//

import SwiftUI

struct HeroSectionView: View {
    let onUploadTap: () -> Void
    let onDemoTap: () -> Void
    
    var body: some View {
        VStack(alignment: .center, spacing: 24) {
            // Premium Tagline Pill
            Text("✨ AI-Powered Photo Restoration")
                .font(FixPixFonts.caption)
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(FixPixColors.accent.opacity(0.1))
                .foregroundColor(FixPixColors.accent)
                .clipShape(Capsule())
                .overlay(
                    Capsule()
                        .stroke(FixPixColors.accent.opacity(0.2), lineWidth: 1)
                )
            
            // Headline
            VStack(spacing: 8) {
                Text("Restore. Enhance.")
                    .font(FixPixFonts.heroTitle)
                Text("Relive Your Memories.")
                    .font(FixPixFonts.heroTitle)
                    .foregroundColor(FixPixColors.fixpixBrandBlue)
            }
            .multilineTextAlignment(.center)
            
            // Subheadline
            Text("Transform your old, blurry, or damaged photos into high-definition masterpieces with professional AI tools.")
                .font(FixPixFonts.body)
                .foregroundColor(FixPixColors.secondaryText)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 20)
            
            // CTA Buttons
            HStack(spacing: 16) {
                FixPixButton(title: "Upload Image", style: .primary, action: onUploadTap)
                FixPixButton(title: "Explore Demo", style: .secondary, action: onDemoTap)
            }
            .padding(.horizontal, FixPixSpacing.horizontal)
        }
        .padding(.vertical, 40)
    }
}

#Preview {
    HeroSectionView(onUploadTap: {}, onDemoTap: {})
        .preferredColorScheme(.dark)
}
