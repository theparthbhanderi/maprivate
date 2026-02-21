//
//  FixPixSlider.swift
//  FixPix
//

import SwiftUI

struct FixPixSlider: View {
    let label: String
    @Binding var value: Double
    var range: ClosedRange<Double> = 0...1
    var step: Double = 0.01
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text(label)
                    .font(FixPixFonts.button())
                    .foregroundColor(FixPixColors.text)
                
                Spacer()
                
                Text("\(Int(value * 100))")
                    .font(.system(.body, design: .monospaced))
                    .foregroundColor(FixPixColors.fixpixBrandBlue)
                    .fontWeight(.bold)
            }
            
            Slider(value: $value, in: range, step: step)
                .tint(FixPixColors.fixpixBrandBlue)
                .background(
                    Capsule()
                        .fill(FixPixColors.fixpixBrandBlue.opacity(0.1))
                        .frame(height: 4)
                )
        }
    }
}

#Preview {
    ZStack {
        Color.black.ignoresSafeArea()
        FixPixSlider(label: "Intensity", value: .constant(0.75))
            .padding()
    }
}
