//
//  Extensions.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import Foundation
import SwiftUI

extension View {
    func embedInNavigation() -> some View {
        NavigationStack {
            self
        }
    }
}
