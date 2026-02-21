//
//  ProjectCardView.swift
//  FixPix
//
//

import SwiftUI

struct ProjectCardView: View {
    let project: EditProject
    let onTap: () -> Void
    
    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, spacing: 0) {
                // Image Thumbnail
                ZStack(alignment: .topTrailing) {
                    Group {
                        let url = project.mainImage.processedUrl ?? project.mainImage.originalUrl
                        // In real app, proper AsyncImage. For now, mock based on ID or system image
                        Image(uiImage: UIImage(systemName: "photo.fill")?.withTintColor(UIColor(FixPixColors.fixpixBrandBlue)) ?? UIImage())
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(height: 140)
                            .frame(maxWidth: .infinity)
                            .clipped()
                            .background(Color.gray.opacity(0.1))

                    }
                    
                    // Status Badge
                    if !project.history.isEmpty {
                        Text("Edited")
                            .font(.system(size: 10, weight: .bold))
                            .padding(.horizontal, 6)
                            .padding(.vertical, 3)
                            .background(Material.thinMaterial)
                            .foregroundColor(.white)
                            .cornerRadius(4)
                            .padding(8)
                    }
                }
                
                // Info
                VStack(alignment: .leading, spacing: 4) {
                    Text(project.name)
                        .font(FixPixFonts.body)
                        .lineLimit(1)
                        .foregroundColor(FixPixColors.text)
                    
                    Text(project.createdAt.formatted(date: .abbreviated, time: .shortened))
                        .font(.caption)
                        .foregroundColor(FixPixColors.secondaryText)
                }
                .padding(12)
            }
            .background(FixPixColors.cardBackground)
            .cornerRadius(12)
            .shadow(color: Color.black.opacity(0.1), radius: 5, x: 0, y: 2)
        }
        .buttonStyle(ScaleButton())
    }
}

fileprivate struct ScaleButton: ButtonStyle {
    func makeBody(configuration: ButtonStyle.Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.96 : 1)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}

#Preview {
    let mockImage = FixPixImage(id: UUID(), originalUrl: URL(string: "https://example.com")!, processedUrl: nil, createdAt: Date(), metadata: .init(width: 800, height: 600, format: "JPG"))
    let project = EditProject(id: UUID(), name: "Travel Photo 001", mainImage: mockImage, history: [EditAction(id: UUID(), type: .enhance, value: 1.0, timestamp: Date())], createdAt: Date())
    
    ZStack {
        Color.black
        ProjectCardView(project: project, onTap: {})
            .frame(width: 180)
    }
}
