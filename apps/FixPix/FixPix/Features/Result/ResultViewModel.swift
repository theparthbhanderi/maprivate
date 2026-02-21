//
//  ResultViewModel.swift
//  FixPix
//
//

import SwiftUI
import Photos
import Combine

@MainActor
final class ResultViewModel: ObservableObject {
    let image: FixPixImage
    @Published var displayImage: UIImage?
    @Published var originalImage: UIImage?
    
    // UI State
    @Published var isSaving = false
    @Published var saveSuccess = false
    @Published var showShareSheet = false
    @Published var showCompare = false
    @Published var errorMessage: String?
    
    init(image: FixPixImage) {
        self.image = image
        // Mock loading images
        // In real app, load from URLs in `image`
        self.displayImage = UIImage(systemName: "photo.artframe")?.withTintColor(UIColor(FixPixColors.fixpixBrandBlue))
        self.originalImage = UIImage(systemName: "photo")?.withTintColor(.gray)
    }
    
    func saveToGallery() {
        guard let imageToSave = displayImage else { return }
        
        isSaving = true
        
        // Mock async save
        Task {
            try? await Task.sleep(nanoseconds: 1_000_000_000)
            
            // In a real app, use PHPhotoLibrary
            /*
            try await PHPhotoLibrary.shared().performChanges {
                PHAssetChangeRequest.creationRequestForAsset(from: imageToSave)
            }
            */
            
            withAnimation {
                isSaving = false
                saveSuccess = true
            }
            
            // Hide success message after 2 seconds
            try? await Task.sleep(nanoseconds: 2_000_000_000)
            withAnimation {
                saveSuccess = false
            }
        }
    }
    
    func prepareForShare() {
        showShareSheet = true
    }
}
