//
//  EmptyStateView.swift
//  FixPix
//
//

import SwiftUI

struct EmptyStateView: View {
    let icon: String
    let title: String
    let message: String
    let actionTitle: String?
    let action: (() -> Void)?
    
    init(icon: String, title: String, message: String, actionTitle: String? = nil, action: (() -> Void)? = nil) {
        self.icon = icon
        self.title = title
        self.message = message
        self.actionTitle = actionTitle
        self.action = action
    }
    
    var body: some View {
        VStack(spacing: 24) {
            ZStack {
                Circle()
                    .fill(FixPixColors.cardBackground)
                    .frame(width: 120, height: 120)
                    .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 5)
                
                Image(systemName: icon)
                    .font(.system(size: 48))
                    .foregroundColor(FixPixColors.secondaryText)
            }
            
            VStack(spacing: 8) {
                Text(title)
                    .font(FixPixFonts.h3)
                    .foregroundColor(FixPixColors.text)
                
                Text(message)
                    .font(FixPixFonts.body)
                    .foregroundColor(FixPixColors.secondaryText)
                    .multilineTextAlignment(.center)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.horizontal, 32)
            }
            
            if let actionTitle = actionTitle, let action = action {
                Button(action: action) {
                    Text(actionTitle)
                        .font(FixPixFonts.button())
                        .foregroundColor(FixPixColors.fixpixBrandBlue)
                        .padding(.horizontal, 24)
                        .padding(.vertical, 12)
                        .background(FixPixColors.fixpixBrandBlue.opacity(0.1))
                        .cornerRadius(20)
                }
                .buttonStyle(BouncyButtonStyle())
                .padding(.top, 8)
            }
        }
        .padding()
    }
}
