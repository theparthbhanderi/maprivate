//
//  FixPixButton.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

enum FixPixButtonStyle {
    case primary
    case secondary
}

struct FixPixButton: View {
    let title: String
    var style: FixPixButtonStyle = .primary
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(FixPixFonts.button())
                .frame(maxWidth: .infinity)
                .padding(.vertical, 16)
                .background(backgroundView)
                .foregroundColor(foregroundColor)
                .cornerRadius(FixPixSpacing.buttonRadius)
                .overlay(
                    RoundedRectangle(cornerRadius: FixPixSpacing.buttonRadius)
                        .stroke(borderColor, lineWidth: 1)
                )
        }
        .buttonStyle(BouncyButtonStyle())
    }
    
    @ViewBuilder
    private var backgroundView: some View {
        if style == .primary {
            FixPixColors.fixpixBrandBlue
        } else {
            Color.clear
        }
    }
    
    private var foregroundColor: Color {
        style == .primary ? .white : FixPixColors.text
    }
    
    private var borderColor: Color {
        style == .primary ? .clear : FixPixColors.border
    }
}
