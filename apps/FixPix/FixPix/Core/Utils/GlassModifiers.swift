//
//  GlassModifiers.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct FixPixGlass: ViewModifier {
    var cornerRadius: CGFloat = 20
    var material: Material = .ultraThinMaterial
    
    func body(content: Content) -> some View {
        content
            .background(material)
            .cornerRadius(cornerRadius)
            .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 5)
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius)
                    .stroke(
                        LinearGradient(
                            gradient: Gradient(colors: [
                                Color.white.opacity(0.4),
                                Color.white.opacity(0.0)
                            ]),
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        ),
                        lineWidth: 1
                    )
            )
    }
}

extension View {
    func fixpixGlass(cornerRadius: CGFloat = 20, material: Material = .ultraThinMaterial) -> some View {
        self.modifier(FixPixGlass(cornerRadius: cornerRadius, material: material))
    }
}
