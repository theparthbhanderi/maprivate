//
//  ResultView.swift
//  FixPix
//
//

import SwiftUI

struct ResultView: View {
    @StateObject private var viewModel: ResultViewModel
    @EnvironmentObject var router: AppRouter
    
    init(image: FixPixImage) {
        _viewModel = StateObject(wrappedValue: ResultViewModel(image: image))
    }
    
    var body: some View {
        ZStack {
            FixPixColors.background.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Nav Bar
                HStack {
                    Button(action: { router.goBack() }) {
                        Image(systemName: "xmark")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(FixPixColors.text)
                            .padding(10)
                            .background(Color.white.opacity(0.1))
                            .clipShape(Circle())
                    }
                    
                    Spacer()
                    
                    Text("Result")
                        .font(FixPixFonts.h3)
                    
                    Spacer()
                    
                    // Invisible spacer for balance
                    Image(systemName: "xmark")
                        .font(.system(size: 18, weight: .semibold))
                        .padding(10)
                        .opacity(0)
                }
                .padding(.horizontal)
                .padding(.top, 10)
                
                ScrollView {
                    VStack(spacing: 32) {
                        
                        // 1. Hero Preview
                        ZStack(alignment: .bottomTrailing) {
                            if let image = viewModel.showCompare ? viewModel.originalImage : viewModel.displayImage {
                                Image(uiImage: image)
                                    .resizable()
                                    .scaledToFit()
                                    .frame(maxWidth: .infinity)
                                    .cornerRadius(20)
                                    .shadow(color: Color.black.opacity(0.3), radius: 20, x: 0, y: 10)
                                    .padding(.horizontal)
                            } else {
                                Rectangle()
                                    .fill(Color.gray.opacity(0.2))
                                    .frame(height: 400)
                                    .cornerRadius(20)
                                    .padding(.horizontal)
                                    .overlay(ProgressView())
                            }
                            
                            // Compare Button
                            Button(action: {}) {
                                Image(systemName: "square.split.2x1")
                                    .font(.system(size: 16))
                                    .foregroundColor(.white)
                                    .padding(12)
                                    .background(Material.thinMaterial)
                                    .clipShape(Circle())
                                    .shadow(radius: 5)
                            }
                            .padding(30)
                            .onLongPressGesture(minimumDuration: 0.0, pressing: { pressing in
                                withAnimation {
                                    viewModel.showCompare = pressing
                                }
                            }, perform: {})
                        }
                        .padding(.top, 20)
                        
                        // 2. Info Row
                        HStack(spacing: 20) {
                            InfoBadge(icon: "arrow.up.left.and.arrow.down.right", text: "2048 x 2048")
                            InfoBadge(icon: "doc", text: "3.2 MB")
                            InfoBadge(icon: "wand.and.stars", text: "AI Enhanced")
                        }
                        
                        // 3. Export Actions
                        VStack(spacing: 16) {
                            ExportButtonView(
                                title: viewModel.saveSuccess ? "Saved!" : "Save to Photos",
                                icon: viewModel.saveSuccess ? "checkmark" : "square.and.arrow.down",
                                subtitle: viewModel.saveSuccess ? nil : "High Quality • JPG",
                                style: .primary,
                                action: { viewModel.saveToGallery() }
                            )
                            .disabled(viewModel.saveSuccess)
                            
                            HStack(spacing: 16) {
                                ExportButtonView(
                                    title: "Share",
                                    icon: "square.and.arrow.up",
                                    style: .secondary,
                                    action: { viewModel.prepareForShare() }
                                )
                                
                                ExportButtonView(
                                    title: "Home",
                                    icon: "house",
                                    style: .secondary,
                                    action: { router.popToRoot() }
                                )
                            }
                        }
                        .padding(.horizontal)
                    }
                    .padding(.bottom, 40)
                }
            }
            
            // Success Overlay (Optional subtle Toast)
            if viewModel.saveSuccess {
                VStack {
                    Spacer()
                    HStack {
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(FixPixColors.fixpixGreen)
                        Text("Saved to Gallery")
                            .font(FixPixFonts.body)
                    }
                    .padding()
                    .background(Material.thinMaterial)
                    .cornerRadius(30)
                    .shadow(radius: 10)
                    .padding(.bottom, 50)
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                }
                .zIndex(100)
            }
        }
        .sheet(isPresented: $viewModel.showShareSheet) {
            if let image = viewModel.displayImage {
                ShareSheet(activityItems: [image])
            }
        }
    }
}

struct InfoBadge: View {
    let icon: String
    let text: String
    
    var body: some View {
        HStack(spacing: 6) {
            Image(systemName: icon)
                .font(.caption)
            Text(text)
                .font(.caption)
                .fontWeight(.medium)
        }
        .foregroundColor(.gray)
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
        .background(Color.white.opacity(0.05))
        .cornerRadius(20)
    }
}

#Preview {
    let mockImage = FixPixImage(id: UUID(), originalUrl: URL(string: "https://example.com")!, processedUrl: nil, createdAt: Date(), metadata: .init(width: 800, height: 600, format: "JPG"))
    ResultView(image: mockImage)
        .environmentObject(AppRouter())
        .preferredColorScheme(.dark)
}
