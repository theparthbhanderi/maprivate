//
//  SettingRowView.swift
//  FixPix
//
//

import SwiftUI

struct SettingRowView<Content: View>: View {
    let icon: String
    let color: Color
    let title: String
    let content: Content
    
    init(icon: String, color: Color, title: String, @ViewBuilder content: () -> Content) {
        self.icon = icon
        self.color = color
        self.title = title
        self.content = content()
    }
    
    var body: some View {
        HStack(spacing: 16) {
            Image(systemName: icon)
                .font(.system(size: 18))
                .foregroundColor(.white)
                .frame(width: 32, height: 32)
                .background(color)
                .cornerRadius(8)
            
            Text(title)
                .font(FixPixFonts.body)
                .foregroundColor(FixPixColors.text)
            
            Spacer()
            
            content
        }
        .padding(.vertical, 4)
    }
}

// Convenience for standard toggle
struct SettingToggleRow: View {
    let icon: String
    let color: Color
    let title: String
    @Binding var isOn: Bool
    
    var body: some View {
        SettingRowView(icon: icon, color: color, title: title) {
            Toggle("", isOn: $isOn)
                .labelsHidden()
        }
    }
}

// Convenience for standard navigation link style
struct SettingLinkRow: View {
    let icon: String
    let color: Color
    let title: String
    let label: String?
    let action: () -> Void
    
    init(icon: String, color: Color, title: String, label: String? = nil, action: @escaping () -> Void) {
        self.icon = icon
        self.color = color
        self.title = title
        self.label = label
        self.action = action
    }
    
    var body: some View {
        Button(action: action) {
            SettingRowView(icon: icon, color: color, title: title) {
                HStack {
                    if let label = label {
                        Text(label)
                            .foregroundColor(FixPixColors.secondaryText)
                    }
                    Image(systemName: "chevron.right")
                        .font(.caption)
                        .foregroundColor(FixPixColors.secondaryText)
                }
            }
        }
    }
}
