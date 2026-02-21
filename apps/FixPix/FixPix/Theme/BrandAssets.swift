//
//  BrandAssets.swift
//  FixPix
//
//

import SwiftUI

struct BrandAssets {
    // Gradients
    static let primaryGradient = LinearGradient(
        colors: [FixPixColors.fixpixBrandBlue, FixPixColors.accent],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )
    
    static let darkGradient = LinearGradient(
        colors: [Color(hex: "1a1a1a"), Color.black],
        startPoint: .top,
        endPoint: .bottom
    )
    
    static let glassGradient = LinearGradient(
        colors: [Color.white.opacity(0.15), Color.white.opacity(0.05)],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )
    
    // Shadows
    static let glowShadow = Color.blue.opacity(0.5)
}
