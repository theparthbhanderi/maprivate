//
//  FixPixAlertView.swift
//  FixPix
//
//

import SwiftUI

struct FixPixAlertView: View {
    @ObservedObject var alertContext: AlertContext
    
    var body: some View {
        ZStack {
            // Un-interactive dimmer background
            Color.black.opacity(0.4)
                .ignoresSafeArea()
                .onTapGesture {
                    // Optional: dismiss on tap outside
                }
            
            VStack(spacing: 24) {
                // Icon Header
                ZStack {
                    Circle()
                        .fill(iconBgColor.opacity(0.2))
                        .frame(width: 64, height: 64)
                    
                    Image(systemName: iconName)
                        .font(.system(size: 28))
                        .foregroundColor(iconColor)
                }
                
                // Content
                VStack(spacing: 8) {
                    Text(alertContext.title)
                        .font(FixPixFonts.h3)
                        .foregroundColor(FixPixColors.text)
                        
                    Text(alertContext.message)
                        .font(FixPixFonts.body)
                        .foregroundColor(FixPixColors.secondaryText)
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)
                }
                
                // Actions
                VStack(spacing: 12) {
                    Button(action: {
                        HapticManager.shared.impact(style: .medium)
                        alertContext.dismiss()
                        alertContext.primaryAction?()
                    }) {
                        Text(alertContext.primaryButtonTitle)
                            .font(FixPixFonts.button())
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 14)
                            .background(primaryButtonColor)
                            .foregroundColor(.white)
                            .cornerRadius(12)
                    }
                    .buttonStyle(BouncyButtonStyle())
                    
                    if let secondaryTitle = alertContext.secondaryButtonTitle {
                        Button(action: {
                            alertContext.dismiss()
                            alertContext.secondaryAction?()
                        }) {
                            Text(secondaryTitle)
                                .font(FixPixFonts.button())
                                .foregroundColor(FixPixColors.secondaryText)
                        }
                        .padding(.top, 4)
                    }
                }
            }
            .padding(32)
            .frame(maxWidth: 340)
            .background(FixPixColors.cardBackground)
            .cornerRadius(24)
            .shadow(color: Color.black.opacity(0.2), radius: 20, x: 0, y: 10)
            .scaleEffect(alertContext.isShowing ? 1 : 0.8)
            .opacity(alertContext.isShowing ? 1 : 0)
            .animation(AnimationUtils.snappiest, value: alertContext.isShowing)
        }
    }
    
    private var iconName: String {
        switch alertContext.type {
        case .error: return "exclamationmark.triangle.fill"
        case .success: return "checkmark.circle.fill"
        case .warning: return "questionmark.diamond.fill"
        case .info: return "info.circle.fill"
        }
    }
    
    private var iconColor: Color {
        switch alertContext.type {
        case .error: return .red
        case .success: return FixPixColors.fixpixGreen
        case .warning: return .orange
        case .info: return FixPixColors.fixpixBrandBlue
        }
    }
    
    private var iconBgColor: Color {
        iconColor
    }
    
    private var primaryButtonColor: Color {
        switch alertContext.type {
        case .error: return .red
        case .warning: return .orange
        default: return FixPixColors.fixpixBrandBlue
        }
    }
}
