//
//  SettingsView.swift
//  FixPix
//
//

import SwiftUI

struct SettingsView: View {
    @StateObject private var viewModel = SettingsViewModel()
    @EnvironmentObject var router: AppRouter
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var demoManager: DemoModeManager
    
    var body: some View {
        ZStack {
            FixPixColors.background.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Nav Bar
                HStack {
                    Button(action: { router.goBack() }) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(FixPixColors.text)
                    }
                    
                    Spacer()
                    
                    Text("Settings")
                        .font(FixPixFonts.h3)
                    
                    Spacer()
                    
                    // Invisible spacer for balance
                    Image(systemName: "chevron.left")
                        .font(.system(size: 18, weight: .semibold))
                        .opacity(0)
                }
                .padding()
                
                ScrollView {
                    VStack(spacing: 24) {
                        
                        // 1. Profile Mini Card
                        HStack(spacing: 16) {
                            Circle()
                                .fill(FixPixColors.fixpixBrandBlue)
                                .frame(width: 60, height: 60)
                                .overlay(Text("P").font(.title2.bold()).foregroundColor(.white))
                            
                            VStack(alignment: .leading, spacing: 4) {
                                Text(viewModel.userName)
                                    .font(FixPixFonts.h3)
                                Text(viewModel.userEmail)
                                    .font(.caption)
                                    .foregroundColor(FixPixColors.secondaryText)
                            }
                            
                            Spacer()
                            
                            Button("Edit") {}
                                .font(.caption.bold())
                                .padding(.horizontal, 12)
                                .padding(.vertical, 6)
                                .background(Color.white.opacity(0.1))
                                .cornerRadius(16)
                        }
                        .padding(20)
                        .background(FixPixColors.cardBackground)
                        .cornerRadius(16)
                        .padding(.horizontal)
                        
                        // 2. App Preferences
                        VStack(spacing: 0) {
                            SectionHeader(title: "APP PREFERENCES")
                            
                            VStack(spacing: 16) {
                                // Theme Selector
                                SettingRowView(icon: themeManager.currentTheme.icon, color: .purple, title: "Theme") {
                                    Picker("Theme", selection: $themeManager.currentTheme) {
                                        ForEach(AppTheme.allCases) { theme in
                                            Text(theme.rawValue).tag(theme)
                                        }
                                    }
                                    .pickerStyle(.menu)
                                    .accentColor(FixPixColors.fixpixBrandBlue)
                                }
                                
                                Divider().background(Color.white.opacity(0.1))
                                
                                SettingRowView(icon: "photo.on.rectangle", color: .blue, title: "Export Quality") {
                                    Picker("Quality", selection: $viewModel.imageQuality) {
                                        Text("Standard").tag("Standard")
                                        Text("High").tag("High")
                                        Text("Lossless").tag("Lossless")
                                    }
                                    .pickerStyle(.menu)
                                    .accentColor(FixPixColors.fixpixBrandBlue)
                                }
                                
                                Divider().background(Color.white.opacity(0.1))
                                
                                SettingToggleRow(icon: "arrow.down.doc.fill", color: .green, title: "Auto-Save to Photos", isOn: $viewModel.autoSave)
                                
                                Divider().background(Color.white.opacity(0.1))
                                
                                SettingToggleRow(icon: "iphone.radiowaves.left.and.right", color: .orange, title: "Haptics", isOn: $viewModel.hapticsEnabled)
                            }
                            .padding(16)
                            .background(FixPixColors.cardBackground)
                            .cornerRadius(16)
                        }
                        .padding(.horizontal)
                        
                        // 3. Editor Preferences
                        VStack(spacing: 0) {
                            SectionHeader(title: "EDITOR")
                            
                            VStack(spacing: 16) {
                                SettingRowView(icon: "wand.and.stars", color: .pink, title: "Default Strength") {
                                    Text("\(Int(viewModel.defaultEnhanceStrength * 100))%")
                                        .foregroundColor(.gray)
                                }
                                
                                Divider().background(Color.white.opacity(0.1))
                                
                                SettingToggleRow(icon: "hand.draw.fill", color: .indigo, title: "Compare Gesture", isOn: $viewModel.compareGestureEnabled)
                            }
                            .padding(16)
                            .background(FixPixColors.cardBackground)
                            .cornerRadius(16)
                        }
                        .padding(.horizontal)
                        
                        // 4. Support
                        VStack(spacing: 0) {
                            SectionHeader(title: "SUPPORT")
                            
                            VStack(spacing: 0) {
                                SettingLinkRow(icon: "questionmark.circle.fill", color: .blue, title: "Help Center", action: {})
                                Divider().padding(.leading, 64).background(Color.white.opacity(0.1))
                                SettingLinkRow(icon: "lock.fill", color: .gray, title: "Privacy Policy", action: {})
                                Divider().padding(.leading, 64).background(Color.white.opacity(0.1))
                                SettingLinkRow(icon: "doc.text.fill", color: .gray, title: "Terms of Service", action: {})
                            }
                            .padding(16)
                            .background(FixPixColors.cardBackground)
                            .cornerRadius(16)
                        }
                        .padding(.horizontal)
                        
                        // Demo Controls (Only visible in Demo Mode)
                        if demoManager.isDemoActive {
                            VStack(spacing: 0) {
                                SectionHeader(title: "DEMO MODE")
                                Button(action: { demoManager.resetDemoSession() }) {
                                    Text("Reset Demo Session")
                                        .font(FixPixFonts.button())
                                        .foregroundColor(.orange)
                                        .frame(maxWidth: .infinity)
                                        .padding()
                                        .background(FixPixColors.cardBackground)
                                        .cornerRadius(16)
                                }
                            }
                            .padding(.horizontal)
                        }
                        
                        // 5. Logout
                        Button(action: { viewModel.logout() }) {
                            Text("Sign Out")
                                .font(FixPixFonts.button())
                                .foregroundColor(.red)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(FixPixColors.cardBackground)
                                .cornerRadius(16)
                        }
                        .padding(.horizontal)
                        
                        Text("Version 1.0.0 (Build 100)")
                            .font(.caption)
                            .foregroundColor(FixPixColors.secondaryText)
                            .padding(.bottom, 20)
                    }
                    .padding(.bottom, 40)
                }
            }
        }
        .navigationBarHidden(true)
    }
}

struct SectionHeader: View {
    let title: String
    
    var body: some View {
        HStack {
            Text(title)
                .font(.caption.bold())
                .foregroundColor(FixPixColors.secondaryText)
            Spacer()
        }
        .padding(.horizontal, 8)
        .padding(.bottom, 8)
    }
}

#Preview {
    SettingsView()
        .environmentObject(AppRouter())
        .environmentObject(ThemeManager())
}
