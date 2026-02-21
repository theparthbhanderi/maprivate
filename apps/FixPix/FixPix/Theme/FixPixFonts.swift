//
//  FixPixFonts.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct FixPixFonts {
    static var h1: Font {
        .system(size: 36, weight: .bold, design: .default)
    }
    
    static var h2: Font {
        .system(size: 28, weight: .bold, design: .default)
    }
    
    static var h3: Font {
        .system(size: 20, weight: .semibold, design: .default)
    }
    
    // Legacy support as properties
    static var heroTitle: Font { h1 }
    static var sectionHeader: Font { h2 }
    static var body: Font { .system(size: 17, weight: .regular, design: .default) }
    static var caption: Font { .system(size: 13, weight: .medium, design: .default) }
    
    static func button() -> Font {
        .system(size: 17, weight: .semibold, design: .default)
    }
}
