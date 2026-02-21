//
//  APIService.swift
//  FixPix
//

import Foundation

class APIService {
    static let shared = APIService()
    private init() {}
    
    func fetchUser() async throws -> User {
        try await Task.sleep(nanoseconds: 1_000_000_000)
        return User(id: "1", name: "Parth Bhanderi", email: "parth@example.com", avatarUrl: nil, isPremium: true)
    }
}

//
//  ImageProcessingService.swift
//

import Foundation
import UIKit
import Combine

final class ImageProcessingService: ObservableObject {
    @Published var isProcessing = false
    @Published var progress: Double = 0.0
    
    func processImage(_ image: UIImage, with tool: ToolType) async throws -> UIImage {
        DispatchQueue.main.async {
            self.isProcessing = true
            self.progress = 0.0
        }
        
        // Mock processing delay
        for i in 1...10 {
            try await Task.sleep(nanoseconds: 500_000_000)
            DispatchQueue.main.async {
                self.progress = Double(i) / 10.0
            }
        }
        
        DispatchQueue.main.async {
            self.isProcessing = false
        }
        
        return image // Simple mock: return same image
    }
}
