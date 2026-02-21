//
//  DemoModeManager.swift
//  FixPix
//
//

import SwiftUI
import Combine

class DemoModeManager: ObservableObject {
    static let shared = DemoModeManager()
    
    @Published var isDemoActive: Bool = true
    
    // Sample Images for Reset
    let sampleImageNames = ["sample1", "sample2", "sample3"]
    
    func resetDemoSession() {
        Logger.log("Resetting Demo Session...", type: .system)
        // Clear caches or reset state if needed
        HapticManager.shared.notification(type: .success)
        Logger.log("Demo Session Reset Complete", type: .system)
    }
    
    func injectSampleData() {
        Logger.log("Injecting Sample Data...", type: .system)
        // Logic to inject sample projects into CoreData/Model would go here
    }
}
