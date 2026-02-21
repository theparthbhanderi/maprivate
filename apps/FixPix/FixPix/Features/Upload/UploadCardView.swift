//
//  UploadCardView.swift
//  FixPix
//

import SwiftUI

struct UploadCardView: View {
    let isLoading: Bool
    
    var body: some View {
        VStack(spacing: 20) {
            if isLoading {
                ProgressView()
                    .scaleEffect(1.5)
                    .tint(FixPixColors.fixpixBrandBlue)
            } else {
                ZStack {
                    // Outer dashed border
                    RoundedRectangle(cornerRadius: FixPixSpacing.cornerRadius)
                        .strokeBorder(style: StrokeStyle(lineWidth: 2, dash: [12, 8]))
                        .foregroundColor(FixPixColors.fixpixBrandBlue.opacity(0.4))
                    
                    VStack(spacing: 16) {
                        // Upload Icon with soft glow
                        ZStack {
                            Circle()
                                .fill(FixPixColors.fixpixBrandBlue.opacity(0.1))
                                .frame(width: 80, height: 80)
                            
                            Image(systemName: "icloud.and.arrow.up")
                                .font(.system(size: 32, weight: .semibold))
                                .foregroundColor(FixPixColors.fixpixBrandBlue)
                        }
                        
                        VStack(spacing: 8) {
                            Text("Upload Your Image")
                                .font(FixPixFonts.sectionHeader)
                                .foregroundColor(FixPixColors.text)
                            
                            Text("Tap to choose a photo or drag it here")
                                .font(FixPixFonts.body)
                                .foregroundColor(FixPixColors.secondaryText)
                                .multilineTextAlignment(.center)
                        }
                    }
                    .padding(32)
                }
            }
        }
        .frame(maxWidth: .infinity)
        .frame(height: 320)
        .background(
            FixPixColors.cardBackground.opacity(0.5)
                .blur(radius: 0.5)
        )
        .cornerRadius(FixPixSpacing.cornerRadius)
        .fixpixGlassPanel()
    }
}

#Preview {
    UploadCardView(isLoading: false)
        .padding()
        .preferredColorScheme(.dark)
}
