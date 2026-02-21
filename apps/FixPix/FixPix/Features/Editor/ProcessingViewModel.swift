//
//  ProcessingViewModel.swift
//  FixPix
//
//

import SwiftUI
import Combine

enum ProcessingState: Equatable {
    case idle
    case processing(String) // Message: "Enhancing...", "Removing scratches..."
    case success
    case error(String)
}

final class ProcessingViewModel: ObservableObject {
    @Published var state: ProcessingState = .idle
    @Published var progress: Double = 0.0
    
    private var processTask: Task<Void, Never>?
    private var progressTimer: Timer?
    
    // Configuration for simulation
    var simulateRandomFailure: Bool = true
    var simulationDuration: TimeInterval = 3.0
    
    @MainActor
    func startProcessing(message: String, duration: TimeInterval? = nil, shouldFail: Bool? = nil) {
        // Reset state
        state = .processing(message)
        progress = 0.0
        
        let actualDuration = duration ?? simulationDuration
        let willFail = shouldFail ?? (simulateRandomFailure && Double.random(in: 0...1) < 0.25)
        
        // Cancel existing task
        processTask?.cancel()
        
        processTask = Task { [weak self] in
            let steps = 50
            let stepDuration = actualDuration / Double(steps)
            
            for i in 1...steps {
                if Task.isCancelled { return }
                
                // Non-linear progress for realism (fast start, slow finish)
                let progressValue = Double(i) / Double(steps)
                // easeOutQuad: 1 - (1-t) * (1-t)
                let curvedProgress = 1.0 - (1.0 - progressValue) * (1.0 - progressValue)
                
                guard let self = self else { return }
                
                withAnimation(.linear(duration: stepDuration)) {
                   self.progress = curvedProgress
                }
                
                try? await Task.sleep(nanoseconds: UInt64(stepDuration * 1_000_000_000))
            }
            
            if Task.isCancelled { return }
            
            guard let self = self else { return }
            
            if willFail {
                // Simulate network error random messages
                let errorMessages = [
                    "Connection timeout. Please check your internet.",
                    "AI Server overloaded. Please try again.",
                    "Failed to process image. Unknown error."
                ]
                state = .error(errorMessages.randomElement() ?? "Processing failed")
            } else {
                state = .success
                // We keep it in success state until the parent dismisses it
                // triggering the after-effect (showing the result)
            }
        }
    }
    
    func cancel() {
        processTask?.cancel()
        state = .idle
        progress = 0.0
    }
    
    func retry(message: String) {
        startProcessing(message: message)
    }
    
    func reset() {
        cancel()
    }
}
