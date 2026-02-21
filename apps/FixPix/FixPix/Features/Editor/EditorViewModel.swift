//
//  EditorViewModel.swift
//  FixPix
//
//

import SwiftUI
import Combine

@MainActor
final class EditorViewModel: ObservableObject {
    @Published var project: EditProject
    @Published var selectedImage: UIImage?
    
    // UI State
    @Published var selectedTool: ToolType?
    @Published var isShowingComparison = false
    
    // Processing State
    @Published var processingViewModel = ProcessingViewModel()
    
    // History
    @Published var canUndo = false
    @Published var canRedo = false
    
    // Transform State (Zoom/Pan)
    @Published var scale: CGFloat = 1.0
    @Published var lastScale: CGFloat = 1.0
    @Published var offset: CGSize = .zero
    @Published var lastOffset: CGSize = .zero
    
    private var originalImage: UIImage?
    
    init(project: EditProject) {
        self.project = project
        // In a real app, we'd load the image from the URL
        // For now, we assume the image was passed into the project
        // Mock loading original logic if needed
        
        // Setup original image for comparison
        self.originalImage = UIImage(systemName: "photo") // Placeholder
    }
    
    var isProcessing: Bool {
        if case .idle = processingViewModel.state {
            return false
        }
        return true
    }
    
    // MARK: - Processing Flow
    
    func startToolProcessing(tool: ToolType) {
        // Dismiss tool panel
        withAnimation {
            selectedTool = nil
        }
        
        // Start processing UI
        let message = processingMessage(for: tool)
        processingViewModel.startProcessing(message: message)
    }
    
    func completeProcessing() {
        // Here we would apply the actual image change
        // For now we just reset the UI state
        // In a real app, update selectedImage with the result
        
        withAnimation(.easeOut) {
            processingViewModel.reset()
        }
        
        // Mock: Briefly show comparison to indicate change happened
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.toggleComparison()
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                self.toggleComparison()
            }
        }
    }
    
    func cancelProcessing() {
        processingViewModel.cancel()
    }
    
    private func processingMessage(for tool: ToolType) -> String {
        switch tool {
        case .enhance: return "Enhancing image details..."
        case .restore: return "Restoring old photo..."
        case .scratchRemoval: return "Removing scratches..."
        case .colorization: return "Colorizing B&W photo..."
        case .upscale: return "Upscaling resolution..."
        case .brightness, .contrast, .saturation: return "Adjusting values..."
        }
    }
    
    // MARK: - Standard Actions
    
    func undo() {
        // Implementation logic
        print("Undo Action")
    }
    
    func redo() {
        // Implementation logic
        print("Redo Action")
    }
    
    func toggleComparison() {
        withAnimation(.easeInOut(duration: 0.2)) {
            isShowingComparison.toggle()
        }
    }
    
    func export() {
        // Prepare for export
        print("Exporting Image")
    }
    
    func resetTransform() {
        withAnimation(.spring()) {
            scale = 1.0
            offset = .zero
        }
    }
}
