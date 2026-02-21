//
//  EnvironmentManager.swift
//  FixPix
//
//

import SwiftUI
import Combine
import Foundation

enum AppEnvironment: String, CaseIterable {
    case demo = "Demo Mode"
    case development = "Development"
    case production = "Production"
}

class EnvironmentManager: ObservableObject {
    static let shared = EnvironmentManager()
    
    @Published var currentEnvironment: AppEnvironment = .demo
    
    var isDemoMode: Bool {
        return currentEnvironment == .demo
    }
    
    var apiBaseURL: URL {
        switch currentEnvironment {
        case .demo:
            return URL(string: "mock://api.fixpix.com")! // Virtual URL
        case .development:
            return URL(string: "http://localhost:8000/api/v1")!
        case .production:
            return URL(string: "https://api.fixpix.com/v1")!
        }
    }
    
    // Toggle for Demo / Real
    func setEnvironment(_ env: AppEnvironment) {
        self.currentEnvironment = env
        Logger.log("Switched environment to: \(env.rawValue)", type: .system)
    }
}
