//
//  ProjectsListView.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct ProjectsListView: View {
    @EnvironmentObject var router: AppRouter
    // In a real app, this would use a dedicated ViewModel fetching from CoreData/SwiftData
    // For now, we mock it or reuse ProfileViewModel logic if possible, or just local state for demo
    @State private var projects: [EditProject] = []
    
    let columns = [
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16)
    ]
    
    var body: some View {
        ZStack {
            FixPixColors.background.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Header
                HStack {
                    Text("My Projects")
                        .font(FixPixFonts.h2)
                        .foregroundColor(FixPixColors.text)
                    Spacer()
                    Button(action: { /* Filter/Sort */ }) {
                        Image(systemName: "line.3.horizontal.decrease.circle")
                            .font(.title2)
                            .foregroundColor(FixPixColors.fixpixBrandBlue)
                    }
                }
                .padding()
                
                ScrollView {
                    LazyVGrid(columns: columns, spacing: 16) {
                        ForEach(projects) { project in
                            ProjectCardView(project: project) {
                                router.navigate(to: .editor(project))
                            }
                        }
                        
                        // Add some mock projects if empty for demo visual
                        if projects.isEmpty {
                            ForEach(0..<6) { _ in
                                MockProjectCard()
                            }
                        }
                    }
                    .padding()
                }
            }
        }
        .onAppear {
            // Load mock data
            loadMockProjects()
        }
    }
    
    private func loadMockProjects() {
        // Create some dummy projects
        let mockImage = FixPixImage(
            id: UUID(),
            originalUrl: URL(string: "https://example.com")!,
            processedUrl: nil,
            createdAt: Date(),
            metadata: .init(width: 100, height: 100, format: "JPG")
        )
        self.projects = [
            EditProject(id: UUID(), name: "Sunset Beach", mainImage: mockImage, history: [], createdAt: Date()),
            EditProject(id: UUID(), name: "Family Portrait", mainImage: mockImage, history: [EditAction(id: UUID(), type: .enhance, value: 1, timestamp: Date())], createdAt: Date().addingTimeInterval(-86400)),
             EditProject(id: UUID(), name: "Old Photo Scan", mainImage: mockImage, history: [], createdAt: Date().addingTimeInterval(-172800))
        ]
    }
}

struct MockProjectCard: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Rectangle()
                .fill(Color.gray.opacity(0.1))
                .aspectRatio(1, contentMode: .fit)
                .overlay(
                    Image(systemName: "photo")
                        .font(.largeTitle)
                        .foregroundColor(.gray.opacity(0.5))
                )
            
            VStack(alignment: .leading, spacing: 4) {
                Text("Project Name")
                    .font(FixPixFonts.body)
                    .foregroundColor(FixPixColors.text)
                    .redacted(reason: .placeholder)
                Text("Just now")
                    .font(.caption)
                    .foregroundColor(FixPixColors.secondaryText)
                    .redacted(reason: .placeholder)
            }
            .padding(12)
        }
        .background(FixPixColors.cardBackground)
        .cornerRadius(12)
    }
}

#Preview {
    ProjectsListView()
        .environmentObject(AppRouter())
        .preferredColorScheme(.dark)
}
