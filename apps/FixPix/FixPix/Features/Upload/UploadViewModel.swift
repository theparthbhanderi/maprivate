//
//  UploadViewModel.swift
//  FixPix
//

import SwiftUI
import Combine
import PhotosUI

@MainActor
final class UploadViewModel: ObservableObject {
    @Published var selectedItem: PhotosPickerItem? {
        didSet {
            handleItemSelection(selectedItem)
        }
    }
    
    @Published var selectedImage: UIImage?
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    // Callback for navigation
    var onImageSelected: ((UIImage) -> Void)?
    
    private func handleItemSelection(_ item: PhotosPickerItem?) {
        guard let item = item else { return }
        
        isLoading = true
        errorMessage = nil
        
        Task {
            if let data = try? await item.loadTransferable(type: Data.self),
               let image = UIImage(data: data) {
                self.selectedImage = image
                self.isLoading = false
                self.onImageSelected?(image)
            } else {
                self.isLoading = false
                self.errorMessage = "Failed to load image. Please try again."
            }
        }
    }
}
