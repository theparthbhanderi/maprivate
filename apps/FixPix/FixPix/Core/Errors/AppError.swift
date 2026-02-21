//
//  AppError.swift
//  FixPix
//
//

import SwiftUI
import Combine
import Foundation

enum AppError: LocalizedError, Identifiable {
    case uploadFailed
    case processingFailed(String)
    case exportFailed
    case networkError
    case unknown
    
    var id: String { localizedDescription }
    
    var errorDescription: String? {
        switch self {
        case .uploadFailed: return "Failed to upload image. Please try again."
        case .processingFailed(let msg): return msg
        case .exportFailed: return "Could not save image to gallery."
        case .networkError: return "No internet connection. Please check your settings."
        case .unknown: return "An unknown error occurred."
        }
    }
    
    var title: String {
        switch self {
        case .uploadFailed: return "Upload Failed"
        case .processingFailed: return "Processing Error"
        case .exportFailed: return "Export Failed"
        case .networkError: return "Connection Error"
        case .unknown: return "Error"
        }
    }
}

// Global Alert State Manager
class AlertContext: ObservableObject {
    @Published var isShowing = false
    @Published var title = ""
    @Published var message = ""
    @Published var type: AlertType = .error
    @Published var primaryAction: (() -> Void)? = nil
    @Published var secondaryAction: (() -> Void)? = nil
    @Published var primaryButtonTitle = "OK"
    @Published var secondaryButtonTitle: String? = nil
    
    enum AlertType {
        case success
        case error
        case warning
        case info
    }
    
    func showAlert(error: AppError) {
        self.title = error.title
        self.message = error.localizedDescription
        self.type = .error
        self.primaryButtonTitle = "OK"
        self.primaryAction = nil
        self.secondaryButtonTitle = nil
        self.isShowing = true
    }
    
    func show(title: String, message: String, type: AlertType = .info, buttonTitle: String = "OK", action: (() -> Void)? = nil) {
        self.title = title
        self.message = message
        self.type = type
        self.primaryButtonTitle = buttonTitle
        self.primaryAction = action
        self.secondaryButtonTitle = nil
        self.isShowing = true
    }
    
    func showConfirmation(title: String, message: String, destructive: Bool = false, confirmTitle: String = "Confirm", action: @escaping () -> Void) {
        self.title = title
        self.message = message
        self.type = destructive ? .error : .warning
        self.primaryButtonTitle = confirmTitle
        self.primaryAction = action
        self.secondaryButtonTitle = "Cancel"
        self.secondaryAction = { self.dismiss() }
        self.isShowing = true
    }
    
    func dismiss() {
        self.isShowing = false
    }
}
