//
//  EditorView.swift
//
//

import SwiftUI

struct EditorView: View {
    @StateObject private var viewModel: EditorViewModel
    @EnvironmentObject var router: AppRouter
    @Environment(\.dismiss) var dismiss
    
    init(project: EditProject) {
        _viewModel = StateObject(wrappedValue: EditorViewModel(project: project))
    }
    
    var body: some View {
        ZStack(alignment: .bottom) {
            // 1. Studio Canvas / Comparison (Bottom Layer)
            Group {
                if viewModel.isShowingComparison, let original = viewModel.selectedImage {
                    BeforeAfterCompareView(originalImage: original, editedImage: original) // Mock: Using same image for now
                        .transition(.opacity)
                } else {
                    ImageCanvasView(viewModel: viewModel)
                        .onTapGesture {
                            if !viewModel.isProcessing {
                                withAnimation(.spring()) {
                                    viewModel.selectedTool = nil
                                }
                            }
                        }
                        .transition(.opacity)
                }
            }
            .blur(radius: viewModel.isProcessing ? 10 : 0) // Blur canvas when processing
            .animation(.easeInOut, value: viewModel.isProcessing)
            
            // 2. UI Controls Layer
            VStack(spacing: 0) {
                // Top Control Bar
                EditorTopBarView(
                    viewModel: viewModel,
                    onBack: { dismiss() },
                    onExport: {
                        // In a real app, we would save the edited state to a new FixPixImage here
                        // For now we pass the project's image to the result screen
                        router.navigate(to: .result(viewModel.project.mainImage))
                    }
                )
                .opacity(viewModel.isProcessing ? 0 : 1) // Hide top bar during processing
                
                Spacer()
                
                // Dynamic Tool Panel (Slides up when tool selected)
                if let tool = viewModel.selectedTool {
                    ToolPanelView(
                        viewModel: ToolPanelViewModel(tool: tool),
                        onClose: {
                            withAnimation(.spring()) {
                                viewModel.selectedTool = nil
                            }
                        },
                        onApply: {
                            viewModel.startToolProcessing(tool: tool)
                        }
                    )
                    .transition(.move(edge: .bottom))
                    .zIndex(1)
                }
                
                // Primary Tool Navigation (Always visible unless processing)
                if !viewModel.isProcessing {
                    ToolBarView(viewModel: viewModel)
                        .padding(.bottom, 20)
                        .zIndex(0)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                }
            }
            .disabled(viewModel.isProcessing)
            
            // 3. Processing Overlay (Top-most Layer)
            if viewModel.isProcessing {
                ProcessingOverlayView(
                    viewModel: viewModel.processingViewModel,
                    onCancel: {
                        viewModel.cancelProcessing()
                    },
                    onRetry: {
                        // Retry with a generic message or context if available
                        viewModel.processingViewModel.retry(message: "Retrying operation...")
                    },
                    onSuccess: {
                        viewModel.completeProcessing()
                    }
                )
                .transition(.opacity)
                .zIndex(100)
            }
        }
        .navigationBarHidden(true)
        .ignoresSafeArea(.keyboard)
        .preferredColorScheme(.dark)
        .onAppear {
            // Mock: Provide image if not already there (for preview/test)
            if viewModel.selectedImage == nil {
                viewModel.selectedImage = UIImage(systemName: "photo.fill")?.withTintColor(.gray)
            }
        }
    }
}

#Preview {
    let mockImage = FixPixImage(id: UUID(), originalUrl: URL(string: "https://example.com")!, processedUrl: nil, createdAt: Date(), metadata: .init(width: 800, height: 600, format: "JPG"))
    let project = EditProject(id: UUID(), name: "Sample Restore", mainImage: mockImage, history: [], createdAt: Date())
    
    NavigationStack {
        EditorView(project: project)
            .environmentObject(AppRouter())
    }
}
