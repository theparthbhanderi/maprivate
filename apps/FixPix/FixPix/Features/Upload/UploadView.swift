//
//  UploadView.swift
//  FixPix
//

import SwiftUI
import PhotosUI

struct UploadView: View {
    @StateObject private var viewModel = UploadViewModel()
    @EnvironmentObject var router: AppRouter
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        ZStack {
            // Adaptive Background
            FixPixColors.background.ignoresSafeArea()
            
            // Background Glow
            Circle()
                .fill(FixPixColors.fixpixBrandBlue.opacity(0.1))
                .blur(radius: 100)
                .offset(x: 150, y: -200)
            
            VStack(spacing: 0) {
                // Top Nav Bar
                HStack {
                    Button {
                        dismiss()
                    } label: {
                        Image(systemName: "chevron.left")
                            .font(.title3.bold())
                            .foregroundColor(FixPixColors.text)
                    }
                    
                    Spacer()
                    
                    Text("New Project")
                        .font(FixPixFonts.sectionHeader)
                        .foregroundColor(FixPixColors.text)
                    
                    Spacer()
                    
                    // Invisible spacer for balance
                    Image(systemName: "chevron.left")
                        .opacity(0)
                }
                .padding(.horizontal, FixPixSpacing.horizontal)
                .padding(.vertical, 16)
                
                ScrollView(showsIndicators: false) {
                    VStack(spacing: 32) {
                        // Main Upload Area
                        let isLoading = viewModel.isLoading
                        PhotosPicker(selection: $viewModel.selectedItem, matching: .images) {
                            UploadCardView(isLoading: isLoading)
                        }
                        .buttonStyle(.plain)
                        .padding(.top, 20)
                        
                        // Error State
                        if let error = viewModel.errorMessage {
                            Text(error)
                                .font(FixPixFonts.caption)
                                .foregroundColor(.red)
                                .multilineTextAlignment(.center)
                        }
                        
                        // Supporting Section: Tips/Formats
                        VStack(alignment: .leading, spacing: 20) {
                            Text("Tips & Info")
                                .font(FixPixFonts.sectionHeader)
                            
                            HStack(spacing: 16) {
                                TipItemView(icon: "sparkles", title: "AI Optimised", description: "Best results with portrait photos.")
                                TipItemView(icon: "photo.on.rectangle", title: "Formats", description: "Supports JPG, PNG, HEIC.")
                            }
                        }
                        .padding(.top, 20)
                        
                        Spacer()
                        
                        // Secondary CTA
                        PhotosPicker(selection: $viewModel.selectedItem, matching: .images) {
                            FixPixButton(title: "Choose from Photos", style: .primary) {
                                // Handled by PhotosPicker selection
                            }
                        }
                    }
                    .padding(FixPixSpacing.horizontal)
                }
            }
        }
        .navigationBarHidden(true)
        .onAppear {
            setupNavigationCallback()
        }
    }
    
    private func setupNavigationCallback() {
        viewModel.onImageSelected = { image in
            let mockImage = FixPixImage(
                id: UUID(),
                originalUrl: URL(string: "file:///local_tmp")!,
                processedUrl: nil,
                createdAt: Date(),
                metadata: .init(width: Double(image.size.width), height: Double(image.size.height), format: "UIElement")
            )
            let project = EditProject(id: UUID(), name: "Restoration Project", mainImage: mockImage, history: [], createdAt: Date())
            router.navigate(to: .editor(project))
        }
    }
}

struct TipItemView: View {
    let icon: String
    let title: String
    let description: String
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Image(systemName: icon)
                .foregroundColor(FixPixColors.fixpixBrandBlue)
                .font(.title2)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(title)
                    .font(FixPixFonts.button())
                Text(description)
                    .font(FixPixFonts.caption)
                    .foregroundColor(FixPixColors.secondaryText)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .fixpixCard()
    }
}

#Preview {
    UploadView()
        .environmentObject(AppRouter())
        .preferredColorScheme(.dark)
}
