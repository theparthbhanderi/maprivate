//
//  CompareViewModel.swift
//  FixPix
//

import SwiftUI
import Combine

final class CompareViewModel: ObservableObject {
    @Published var sliderPosition: CGFloat = 0.5
    
    func reset() {
        sliderPosition = 0.5
    }
}
