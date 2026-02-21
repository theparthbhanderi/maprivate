//
//  FixPixApp.swift
//  FixPix
//

import SwiftUI

@main
struct FixPixApp: App {

    @StateObject private var processingService = ImageProcessingService()
    @StateObject private var themeManager = ThemeManager()
    @StateObject private var alertContext = AlertContext()
    @StateObject private var demoManager = DemoModeManager.shared
    
    @State private var isLaunching = true
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                if isLaunching {
                    LaunchView()
                        .transition(.opacity)
                        .zIndex(2000)
                } else {
                    ZStack {
                        MainTabView()
                            .environmentObject(themeManager)
                            .environmentObject(demoManager)
                            .environmentObject(processingService)
                            .environmentObject(alertContext)
                            
                        // Global Alert Overlay
                        if alertContext.isShowing {
                            FixPixAlertView(alertContext: alertContext)
                                .transition(.opacity)
                                .zIndex(999)
                        }
                    }
                    .transition(.opacity)
                }
            }
            .preferredColorScheme(themeManager.currentTheme.colorScheme)
            .onAppear {
                // Simulate app initialization
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.5) {
                    withAnimation(.easeOut) {
                        isLaunching = false
                    }
                }
            }
        }
    }
    
    @ViewBuilder
}
