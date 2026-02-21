//
//  HomeViewModel.swift
//  FixPix
//

import SwiftUI
import Combine

class HomeViewModel: ObservableObject {
    @Published var recentProjects: [EditProject] = []
    @Published var isLoading = false
    
    func loadProjects() {
        isLoading = true
        // Mock loading
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            self.isLoading = false
            // Add mock projects if needed
        }
    }
}

//
//  HomeView.swift
//

import SwiftUI

struct HomeView: View {
    @StateObject private var viewModel = HomeViewModel()
    @EnvironmentObject var router: AppRouter
    @EnvironmentObject var tabManager: TabManager
    
    var body: some View {
        ZStack {
            // Adaptive Background
            FixPixColors.background.ignoresSafeArea()
            
            // Subtle Background Glow (AI feel)
            Circle()
                .fill(FixPixColors.fixpixBrandBlue.opacity(0.1))
                .blur(radius: 100)
                .offset(x: -150, y: -200)
            
            ScrollView(showsIndicators: false) {
                VStack(spacing: 0) {
                    // Nav Bar / Logo
                    HStack {
                        VibrantLogoView()
                        Spacer()
                        Button {
                            tabManager.switchTab(to: .profile)
                        } label: {
                            Image(systemName: "person.circle")
                                .font(.title2)
                                .foregroundColor(FixPixColors.text)
                        }
                    }
                    .padding(.horizontal, FixPixSpacing.horizontal)
                    .padding(.top, 10)
                    
                    // Hero Section
                    HeroSectionView(
                        onUploadTap: { tabManager.switchTab(to: .upload) },
                        onDemoTap: { /* Future demo logic */ }
                    )
                    
                    // Comparison Visual
                    ComparisonCardView()
                        .padding(.bottom, 60)
                    
                    // Features Grid
                    FeatureGridView()
                        .padding(.bottom, 80)
                    
                    // Final CTA Section
                    VStack(spacing: 20) {
                        Text("Ready to relive your memories?")
                            .font(FixPixFonts.sectionHeader)
                            .multilineTextAlignment(.center)
                        
                        FixPixButton(title: "Start Restoring Now", style: .primary) {
                            tabManager.switchTab(to: .upload)
                        }
                    }
                    .padding(32)
                    .frame(maxWidth: .infinity)
                    .background(FixPixColors.accent.opacity(0.05))
                    .cornerRadius(FixPixSpacing.cornerRadius)
                    .padding(.horizontal, FixPixSpacing.horizontal)
                    .padding(.bottom, 60)
                }
            }
        }
        .navigationBarHidden(true) // Using custom nav bar
        .onAppear {
            viewModel.loadProjects()
        }
    }
}

struct VibrantLogoView: View {
    var body: some View {
        HStack(spacing: 8) {
            ZStack {
                Circle()
                    .fill(FixPixColors.fixpixBrandBlue)
                    .frame(width: 32, height: 32)
                
                Image(systemName: "sparkles")
                    .foregroundColor(.white)
                    .font(.system(size: 16, weight: .bold))
            }
            
            Text("FixPix")
                .font(.system(size: 22, weight: .black, design: .rounded))
                .foregroundColor(FixPixColors.text)
        }
    }
}

#Preview {
    NavigationStack {
        HomeView()
            .environmentObject(AppRouter())
    }
    .preferredColorScheme(.dark)
}
