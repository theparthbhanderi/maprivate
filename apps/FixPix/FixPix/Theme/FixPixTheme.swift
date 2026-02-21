//
//  FixPixTheme.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct GlassPanelModifier: ViewModifier {
    @Environment(\.colorScheme) var colorScheme
    
    func body(content: Content) -> some View {
        content
            .background(
                RoundedRectangle(cornerRadius: FixPixSpacing.cornerRadius)
                    .fill(colorScheme == .dark ? Color.white.opacity(0.1) : Color.white.opacity(0.7))
                    .background(BlurView(style: colorScheme == .dark ? .systemThinMaterialDark : .systemThinMaterialLight))
            )
            .clipShape(RoundedRectangle(cornerRadius: FixPixSpacing.cornerRadius))
            .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 5)
    }
}

struct FixPixCardModifier: ViewModifier {
    @Environment(\.colorScheme) var colorScheme
    
    func body(content: Content) -> some View {
        content
            .background(FixPixColors.cardBackground)
            .cornerRadius(FixPixSpacing.cornerRadius)
            .shadow(color: Color.black.opacity(0.05), radius: 8, x: 0, y: 4)
    }
}

extension View {
    func fixpixGlassPanel() -> some View {
        modifier(GlassPanelModifier())
    }
    
    func fixpixCard() -> some View {
        modifier(FixPixCardModifier())
    }
}

// Helper for Blur Effect
struct BlurView: UIViewRepresentable {
    var style: UIBlurEffect.Style
    
    func makeUIView(context: Context) -> UIVisualEffectView {
        UIVisualEffectView(effect: UIBlurEffect(style: style))
    }
    
    func updateUIView(_ uiView: UIVisualEffectView, context: Context) {
        uiView.effect = UIBlurEffect(style: style)
    }
}
