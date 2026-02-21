//
//  ProfileViewModel.swift
//  FixPix
//
//

import SwiftUI
import Combine

@MainActor
final class ProfileViewModel: ObservableObject {
    @Published var user: User?
    @Published var projects: [EditProject] = []
    @Published var isLoading = false
    
    init() {
        loadData()
    }
    
    func loadData() {
        isLoading = true
        
        Task {
            // Mock Data Loading
            try? await Task.sleep(nanoseconds: 500_000_000)
            
            self.user = User(
                id: "u1",
                name: "Parth Bhanderi",
                email: "parth.bhanderi@fixpix.com",
                avatarUrl: nil,
                isPremium: true
            )
            
            // Mock Projects
            self.projects = (1...6).map { i in
                let mockImage = FixPixImage(
                    id: UUID(),
                    originalUrl: URL(string: "https://example.com/\(i)")!,
                    processedUrl: i % 2 == 0 ? URL(string: "https://example.com/\(i)_proc") : nil,
                    createdAt: Date().addingTimeInterval(Double(-i * 86400)),
                    metadata: .init(width: 1080, height: 1350, format: "JPG")
                )
                
                let hasEdits = i % 2 == 0
                let history = hasEdits ? [
                    EditAction(id: UUID(), type: .enhance, value: 1.0, timestamp: Date())
                ] : []
                
                return EditProject(
                    id: UUID(),
                    name: "Project #\(100 + i)",
                    mainImage: mockImage,
                    history: history,
                    createdAt: Date().addingTimeInterval(Double(-i * 86400))
                )
            }
            
            self.isLoading = false
        }
    }
    
    func deleteProject(id: UUID) {
        withAnimation {
            projects.removeAll { $0.id == id }
        }
    }
}
