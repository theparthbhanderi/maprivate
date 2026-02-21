//
//  FixPixTabBar.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

enum TabItem: String, CaseIterable {
    case home
    case upload
    case editor
    case projects
    case profile
    
    var iconName: String {
        switch self {
        case .home: return "house.fill"
        case .upload: return "plus.circle.fill"
        case .editor: return "wand.and.stars"
        case .projects: return "square.grid.2x2.fill"
        case .profile: return "person.crop.circle.fill"
        }
    }
    
    var title: String {
        switch self {
        case .home: return "Home"
        case .upload: return "New"
        case .editor: return "Editor"
        case .projects: return "Projects"
        case .profile: return "Profile"
        }
    }
}

struct FixPixTabBar: View {
    @Binding var selectedTab: TabItem
    
    var body: some View {
        HStack {
            ForEach(TabItem.allCases, id: \.self) { tab in
                Spacer()
                
                Button {
                    withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                        selectedTab = tab
                    }
                } label: {
                    VStack(spacing: 4) {
                        Image(systemName: tab.iconName)
                            .font(.system(size: 24))
                            .symbolEffect(.bounce, value: selectedTab == tab)
                        
                        Text(tab.title)
                            .font(.system(size: 10, weight: .medium))
                    }
                    .foregroundColor(selectedTab == tab ? FixPixColors.fixpixBrandBlue : .gray)
                    .scaleEffect(selectedTab == tab ? 1.1 : 1.0)
                }
                
                Spacer()
            }
        }
        .padding(.vertical, 12)
        .padding(.horizontal, 8)
        .background(.ultraThinMaterial)
        .cornerRadius(30)
        .shadow(color: Color.black.opacity(0.15), radius: 10, x: 0, y: 5)
        .padding(.horizontal, 24)
        .overlay(
            RoundedRectangle(cornerRadius: 30)
                .stroke(Color.white.opacity(0.2), lineWidth: 1)
                .padding(.horizontal, 24)
        )
    }
}

#Preview {
    ZStack {
        Color.black.ignoresSafeArea()
        VStack {
            Spacer()
            FixPixTabBar(selectedTab: .constant(.home))
        }
    }
}
