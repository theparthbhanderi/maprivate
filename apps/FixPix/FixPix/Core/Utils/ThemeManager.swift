//
//  ThemeManager.swift
//  FixPix
//
//

import SwiftUI
import Combine

enum AppTheme: String, CaseIterable, Identifiable {
    case system = "System"
    case light = "Light"
    case dark = "Dark"
    
    var id: String { rawValue }
    
    var colorScheme: ColorScheme? {
        switch self {
        case .system: return nil
        case .light: return .light
        case .dark: return .dark
        }
    }
    
    var icon: String {
        switch self {
        case .system: return "iphone"
        case .light: return "sun.max.fill"
        case .dark: return "moon.fill"
        }
    }
}

class ThemeManager: ObservableObject {
    @AppStorage("appTheme") var currentThemeRaw: String = AppTheme.dark.rawValue
    
    var currentTheme: AppTheme {
        get { AppTheme(rawValue: currentThemeRaw) ?? .dark }
        set { currentThemeRaw = newValue.rawValue }
    }
}
