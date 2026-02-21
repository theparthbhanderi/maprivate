//
//  TabManager.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

class TabManager: ObservableObject {
    @Published var selectedTab: TabItem = .home
    
    func switchTab(to tab: TabItem) {
        selectedTab = tab
    }
}
