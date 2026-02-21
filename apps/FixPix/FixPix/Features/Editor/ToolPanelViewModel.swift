//
//  ToolPanelViewModel.swift
//  FixPix
//

import SwiftUI
import Combine

class ToolPanelViewModel: ObservableObject {
    @Published var intensity: Double = 0.8
    @Published var brightness: Double = 0.0
    @Published var contrast: Double = 0.0
    @Published var saturation: Double = 1.0
    @Published var selectedPresetId: String?
    
    let tool: ToolType
    
    init(tool: ToolType) {
        self.tool = tool
        resetToDefaults()
    }
    
    func resetToDefaults() {
        intensity = 0.8
        brightness = 0.0
        contrast = 0.0
        saturation = 1.0
        selectedPresetId = nil
    }
    
    func applyChanges() {
        // Logic to apply these values to the main image via EditorViewModel
        print("Applying \(tool.rawValue) changes with intensity: \(intensity)")
    }
}
