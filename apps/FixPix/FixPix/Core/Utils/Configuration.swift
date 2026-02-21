//
//  Configuration.swift
//  FixPix
//
//

import Foundation

struct Configuration {
    static let appName = "FixPix"
    static let version = "1.0.0"
    static let build = "100"
    
    struct API {
        static var baseUrl: URL { EnvironmentManager.shared.apiBaseURL }
        static let timeout: TimeInterval = 30.0
    }
    
    struct Editor {
        static let maxZoomScale: CGFloat = 5.0
        static let minZoomScale: CGFloat = 0.5
        static let defaultExportQuality: Double = 0.9
    }
    
    struct FeatureFlags {
        static let enableHighResProcessing = true
        static let enableCloudSync = false // Not ready
    }
}
