//
//  LaunchView.swift
//  FixPix
//
//

import SwiftUI

struct LaunchView: View {
    @State private var isAnimating = false
    @State private var showText = false
    
    var body: some View {
        ZStack {
            FixPixColors.fixpixDarkBg.ignoresSafeArea()
            
            VStack(spacing: 20) {
                if showText {
                    FixPixLogo(size: 100, showText: true, textStyle: FixPixFonts.h1)
                        .scaleEffect(isAnimating ? 1.05 : 1.0)
                        .transition(.opacity.combined(with: .move(edge: .bottom)))
                }
            }
        }
        .onAppear {
            withAnimation(.easeInOut(duration: 1.5).repeatForever(autoreverses: true)) {
                isAnimating = true
            }
            
            withAnimation(.easeOut(duration: 0.8).delay(0.3)) {
                showText = true
            }
        }
    }
}

#Preview {
    LaunchView()
}
