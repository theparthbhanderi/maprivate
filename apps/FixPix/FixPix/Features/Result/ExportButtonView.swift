//
//  ExportButtonView.swift
//  FixPix
//
//

import SwiftUI

struct ExportButtonView: View {
    let title: String
    let icon: String
    var subtitle: String? = nil
    var style: ExportButtonStyle = .primary
    let action: () -> Void
    
    enum ExportButtonStyle {
        case primary
        case secondary
        case outline
    }
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Image(systemName: icon)
                    .font(.system(size: 20, weight: .medium))
                
                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(FixPixFonts.button())
                    
                    if let subtitle = subtitle {
                        Text(subtitle)
                            .font(.caption)
                            .opacity(0.7)
                    }
                }
                
                Spacer()
                
                if style == .primary {
                    Image(systemName: "arrow.right")
                        .font(.system(size: 14, weight: .bold))
                        .opacity(0.5)
                }
            }
            .padding()
            .background(backgroundView)
            .foregroundColor(foregroundColor)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(borderColor, lineWidth: 1)
            )
        }
        .buttonStyle(ScaleButtonStyle())
    }
    
    @ViewBuilder
    private var backgroundView: some View {
        switch style {
        case .primary:
            FixPixColors.fixpixBrandBlue
        case .secondary:
            Color.white.opacity(0.1)
        case .outline:
            Color.clear
        }
    }
    
    private var foregroundColor: Color {
        switch style {
        case .primary: return .white
        case .secondary: return .white
        case .outline: return FixPixColors.text
        }
    }
    
    private var borderColor: Color {
        switch style {
        case .primary: return .clear
        case .secondary: return .white.opacity(0.1)
        case .outline: return FixPixColors.border
        }
    }
}

struct ScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: ButtonStyle.Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.98 : 1)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}

#Preview {
    VStack {
        ExportButtonView(title: "Save to Photos", icon: "square.and.arrow.down", subtitle: "High Quality • JPG", style: .primary, action: {})
        ExportButtonView(title: "Share", icon: "square.and.arrow.up", style: .secondary, action: {})
        ExportButtonView(title: "New Project", icon: "plus", style: .outline, action: {})
    }
    .padding()
    .background(Color.black)
}
