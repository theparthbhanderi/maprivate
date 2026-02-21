//
//  SettingsViewModel.swift
//  FixPix
//
//

import SwiftUI
import Combine

class SettingsViewModel: ObservableObject {
    // App Preferences
    @AppStorage("imageQuality") var imageQuality: String = "High"
    @AppStorage("autoSave") var autoSave: Bool = true
    @AppStorage("hapticsEnabled") var hapticsEnabled: Bool = true
    
    // Editor Preferences
    @AppStorage("defaultEnhanceStrength") var defaultEnhanceStrength: Double = 0.8
    @AppStorage("compareGestureEnabled") var compareGestureEnabled: Bool = true
    @AppStorage("autoPreview") var autoPreview: Bool = true
    
    // User Info (Read only mock for now)
    @Published var userName: String = "Parth Bhanderi"
    @Published var userEmail: String = "parth@example.com"
    
    func logout() {
        // Handle logout logic
        print("Logging out...")
    }
}
