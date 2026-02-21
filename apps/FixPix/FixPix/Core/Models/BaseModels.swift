//
//  User.swift
//  FixPixApp
//

import Foundation

struct User: Codable, Identifiable, Hashable {
    let id: String
    let name: String
    let email: String
    let avatarUrl: String?
    let isPremium: Bool
}

//
//  FixPixImage.swift
//

import Foundation

struct FixPixImage: Codable, Identifiable, Hashable {
    let id: UUID
    let originalUrl: URL
    let processedUrl: URL?
    let createdAt: Date
    let metadata: ImageMetadata
}

struct ImageMetadata: Codable, Hashable {
    let width: Double
    let height: Double
    let format: String
}

//
//  EditProject.swift
//

import Foundation

struct EditProject: Codable, Identifiable, Hashable {
    let id: UUID
    let name: String
    let mainImage: FixPixImage
    var history: [EditAction]
    let createdAt: Date
}

struct EditAction: Codable, Identifiable, Hashable {
    let id: UUID
    let type: ToolType
    let value: Double
    let timestamp: Date
}

//
//  ToolType.swift
//

import Foundation

enum ToolType: String, Codable, CaseIterable {
    case enhance = "AI Enhance"
    case restore = "Face Restoration"
    case scratchRemoval = "Scratch Removal"
    case colorization = "Colorization"
    case upscale = "4K Upscaling"
    case brightness = "Brightness"
    case contrast = "Contrast"
    case saturation = "Saturation"
}
