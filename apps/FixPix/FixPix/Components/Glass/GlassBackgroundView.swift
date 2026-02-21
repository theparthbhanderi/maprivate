//
//  GlassBackgroundView.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct GlassBackgroundView: View {
    var body: some View {
        ZStack {
            // Base Background
            FixPixColors.fixpixDarkBg.ignoresSafeArea()
            
            // Subtle Orbs
            GeometryReader { proxy in
                let size = proxy.size
                
                Circle()
                    .fill(FixPixColors.fixpixBrandBlue)
                    .frame(width: size.width * 0.8, height: size.width * 0.8)
                    .blur(radius: 120)
                    .opacity(0.15)
                    .offset(x: -size.width * 0.3, y: -size.height * 0.3)
                
                Circle()
                    .fill(Color.purple)
                    .frame(width: size.width * 0.6, height: size.width * 0.6)
                    .blur(radius: 100)
                    .opacity(0.1)
                    .offset(x: size.width * 0.4, y: size.height * 0.2)
            }
            
            // Ultra Thin Material Overlay for that "Frosted" look on top
            Rectangle()
                .fill(.ultraThinMaterial)
                .opacity(0.1)
                .ignoresSafeArea()
        }
    }
}

#Preview {
    GlassBackgroundView()
}
