//
//  APIClient.swift
//  FixPix
//
//

import Foundation
import UIKit

protocol APIClient {
    func fetchUserProfile() async throws -> User
    func processImage(image: UIImage, tool: ToolType) async throws -> UIImage
    func uploadImage(_ image: UIImage) async throws -> String
}

// Real (Future) Implementation
class RealAPIClient: APIClient {
    func fetchUserProfile() async throws -> User {
        // Implement real network call using EnvironmentManager.shared.apiBaseURL
        throw AppError.networkError // Placeholder
    }
    
    func processImage(image: UIImage, tool: ToolType) async throws -> UIImage {
        // Upload image -> Poll job -> Download result
        // This would connect to the Python backend
        throw AppError.processingFailed("Backend not connected")
    }
    
    func uploadImage(_ image: UIImage) async throws -> String {
        // Generic upload
        throw AppError.uploadFailed
    }
}

// Mock Implementation (Moved logic from MockServices)
class MockAPIClient: APIClient {
    func fetchUserProfile() async throws -> User {
        try await Task.sleep(nanoseconds: 500_000_000)
        return User(id: "demo_user", name: "Parth Bhanderi", email: "parth@example.com", avatarUrl: nil, isPremium: true)
    }
    
    func processImage(image: UIImage, tool: ToolType) async throws -> UIImage {
        // Simulation logic is handled nicely in ProcessingViewModel
        // This is just a direct service call if needed
        try await Task.sleep(nanoseconds: 2_000_000_000)
        return image
    }
    
    func uploadImage(_ image: UIImage) async throws -> String {
        try await Task.sleep(nanoseconds: 1_000_000_000)
        return "mock_image_id_123"
    }
}
