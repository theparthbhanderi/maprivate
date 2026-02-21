//
//  ToolHeaderView.swift
//  FixPix
//

import SwiftUI

struct ToolHeaderView: View {
    let title: String
    let iconName: String
    let onReset: () -> Void
    let onClose: () -> Void
    
    var body: some View {
        HStack(spacing: 16) {
            // Icon + Title
            HStack(spacing: 12) {
                Image(systemName: iconName)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(FixPixColors.fixpixBrandBlue)
                
                Text(title)
                    .font(FixPixFonts.sectionHeader)
                    .foregroundColor(FixPixColors.text)
            }
            
            Spacer()
            
            // Actions
            HStack(spacing: 20) {
                Button(action: onReset) {
                    Text("Reset")
                        .font(FixPixFonts.caption)
                        .foregroundColor(FixPixColors.fixpixBrandBlue)
                }
                
                Button(action: onClose) {
                    ZStack {
                        Circle()
                            .fill(Color.gray.opacity(0.1))
                            .frame(width: 32, height: 32)
                        
                        Image(systemName: "xmark")
                            .font(.system(size: 12, weight: .bold))
                            .foregroundColor(FixPixColors.text)
                    }
                }
            }
        }
        .padding(.horizontal, 24)
        .padding(.vertical, 16)
    }
}
