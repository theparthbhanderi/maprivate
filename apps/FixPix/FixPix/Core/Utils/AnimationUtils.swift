//
//  AnimationUtils.swift
//  FixPix
//
//

import SwiftUI

struct AnimationUtils {
    // Premium Apple-style spring
    static let snappiest = Animation.spring(response: 0.3, dampingFraction: 0.7, blendDuration: 0)
    static let gentle = Animation.spring(response: 0.5, dampingFraction: 0.8, blendDuration: 0)
    static let bouncy = Animation.spring(response: 0.4, dampingFraction: 0.6, blendDuration: 0)
}

// Bouncing Button Style
struct BouncyButtonStyle: ButtonStyle {
    var scale: CGFloat = 0.96
    
    func makeBody(configuration: ButtonStyle.Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? scale : 1.0)
            .animation(AnimationUtils.snappiest, value: configuration.isPressed)
            .onChange(of: configuration.isPressed) { isPressed in
                if isPressed {
                    HapticManager.shared.impact(style: .light)
                }
            }
    }
}

extension View {
    func withPremiumAnimation() -> some View {
        self.transaction { transaction in
            transaction.animation = AnimationUtils.gentle
        }
    }
}
