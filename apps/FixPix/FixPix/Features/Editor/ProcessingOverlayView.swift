//
//  ProcessingOverlayView.swift
//  FixPix
//
//

import SwiftUI

struct ProcessingOverlayView: View {
    @ObservedObject var viewModel: ProcessingViewModel
    var onCancel: () -> Void
    var onRetry: () -> Void
    var onSuccess: () -> Void
    
    @State private var pulse: Bool = false
    
    var body: some View {
        ZStack {
            // 1. Background Blur / Dim
            Color.black.opacity(0.4)
                .ignoresSafeArea()
                .transition(.opacity)
            
            // Background Blur Effect using UIKit bridge standard
            BlurView(style: .systemUltraThinMaterialDark)
                .ignoresSafeArea()
                .opacity(0.8)
            
            // 2. Content Card
            VStack(spacing: 24) {
                switch viewModel.state {
                case .idle:
                    EmptyView()
                    
                case .processing(let message):
                    VStack(spacing: 24) {
                        // Premium Loader
                        ZStack {
                            Circle()
                                .stroke(Color.white.opacity(0.1), lineWidth: 4)
                                .frame(width: 60, height: 60)
                            
                            Circle()
                                .trim(from: 0, to: viewModel.progress)
                                .stroke(
                                    FixPixColors.fixpixBrandBlue,
                                    style: StrokeStyle(lineWidth: 4, lineCap: .round)
                                )
                                .frame(width: 60, height: 60)
                                .rotationEffect(.degrees(-90))
                                .animation(.linear(duration: 0.1), value: viewModel.progress)
                            
                            // Glowing Orb in center
                            Circle()
                                .fill(FixPixColors.fixpixBrandBlue)
                                .frame(width: 10, height: 10)
                                .scaleEffect(pulse ? 1.2 : 0.8)
                                .opacity(pulse ? 1.0 : 0.5)
                                .blur(radius: 4)
                                .onAppear {
                                    withAnimation(.easeInOut(duration: 1.0).repeatForever(autoreverses: true)) {
                                        pulse.toggle()
                                    }
                                }
                        }
                        .padding(.top, 10)
                        
                        VStack(spacing: 8) {
                            Text(message)
                                .font(FixPixFonts.h3)
                                .foregroundColor(.white)
                            
                            Text("\(Int(viewModel.progress * 100))%")
                                .font(FixPixFonts.body)
                                .foregroundColor(.white.opacity(0.7))
                                .monospacedDigit()
                        }
                        
                        // Fake Steps just for visuals
                        HStack(spacing: 4) {
                            ForEach(0..<3) { index in
                                Circle()
                                    .fill(index < Int(viewModel.progress * 3) ? Color.white : Color.white.opacity(0.2))
                                    .frame(width: 6, height: 6)
                            }
                        }
                        .padding(.top, 8)
                    }
                    .padding(40)
                    .fixpixGlassPanel()
                    .transition(.scale.combined(with: .opacity))
                    
                case .success:
                    VStack(spacing: 20) {
                        Image(systemName: "checkmark.circle.fill")
                            .font(.system(size: 60))
                            .foregroundColor(FixPixColors.fixpixGreen)
                            .symbolEffect(.bounce, value: true)
                        
                        Text("Enhancement Complete!")
                            .font(FixPixFonts.h3)
                            .foregroundColor(.white)
                    }
                    .padding(40)
                    .fixpixGlassPanel()
                    .onAppear {
                        HapticManager.shared.notification(type: .success)
                        // Auto-trigger completion after short delay
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                            onSuccess()
                        }
                    }
                    .transition(.scale.combined(with: .opacity))
                    
                case .error(let errorMessage):
                    VStack(spacing: 20) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .font(.system(size: 50))
                            .foregroundColor(.red)
                        
                        VStack(spacing: 8) {
                            Text("Processing Failed")
                                .font(FixPixFonts.h3)
                                .foregroundColor(.white)
                            
                            Text(errorMessage)
                                .font(FixPixFonts.body)
                                .foregroundColor(.white.opacity(0.7))
                                .multilineTextAlignment(.center)
                                .padding(.horizontal)
                        }
                        
                        HStack(spacing: 16) {
                            Button(action: onCancel) {
                                Text("Cancel")
                                    .font(FixPixFonts.button())
                                    .foregroundColor(.white.opacity(0.8))
                                    .padding(.horizontal, 16)
                                    .padding(.vertical, 10)
                                    .background(Color.white.opacity(0.1))
                                    .cornerRadius(8)
                            }
                            
                            Button(action: onRetry) {
                                Text("Try Again")
                                    .font(FixPixFonts.button())
                                    .foregroundColor(.white)
                                    .padding(.horizontal, 20)
                                    .padding(.vertical, 10)
                                    .background(FixPixColors.primary)
                                    .cornerRadius(8)
                            }
                        }
                        .padding(.top, 10)
                    }
                    .padding(32)
                    .fixpixGlassPanel()
                    .onAppear {
                        HapticManager.shared.notification(type: .error)
                    }
                    .transition(.scale.combined(with: .opacity))
                }
            }
        }
        .zIndex(100) // Ensure it lays on top
    }
}

#Preview {
    let vm = ProcessingViewModel()
    ZStack {
        // Background mock
        Color.gray
        
        DrawingContent()
        
        ProcessingOverlayView(
            viewModel: vm,
            onCancel: { print("Cancel") },
            onRetry: { vm.retry(message: "Retrying...") },
            onSuccess: { print("Success") }
        )
    }
    .onAppear {
        vm.startProcessing(message: "Deep Enhancing...", shouldFail: false)
    }
}

struct DrawingContent: View {
    var body: some View {
        VStack {
            Text("Editor Content")
            Spacer()
        }
    }
}
