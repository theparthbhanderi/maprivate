//
//  FixPixLogo.swift
//  FixPix
//
//

import SwiftUI

struct FixPixLogo: View {
    var size: CGFloat = 40
    var showText: Bool = true
    var textStyle: Font = FixPixFonts.h2
    
    var body: some View {
        HStack(spacing: size * 0.25) {
            // Icon
            ZStack {
                RoundedRectangle(cornerRadius: size * 0.25)
                    .fill(BrandAssets.primaryGradient)
                    .frame(width: size, height: size)
                    .shadow(color: BrandAssets.glowShadow, radius: 10, x: 0, y: 5)
                
                Image(systemName: "wand.and.stars.inverse")
                    .font(.system(size: size * 0.5))
                    .foregroundColor(.white)
            }
            
            // Text
            if showText {
                Text(Configuration.appName)
                    .font(textStyle)
                    .foregroundColor(FixPixColors.text)
                    .tracking(1.0) // Slight letter spacing for premium look
            }
        }
    }
}
