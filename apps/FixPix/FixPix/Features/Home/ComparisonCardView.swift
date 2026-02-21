//
//  ComparisonCardView.swift
//  FixPix
//

import SwiftUI

struct ComparisonCardView: View {
    var body: some View {
        VStack(spacing: 0) {
            ZStack(alignment: .center) {
                // Main Photo Container
                RoundedRectangle(cornerRadius: FixPixSpacing.cornerRadius)
                    .fill(Color.gray.opacity(0.1))
                    .frame(height: 300)
                    .overlay(
                        HStack(spacing: 0) {
                            // Before Side
                            Rectangle()
                                .fill(Color.black.opacity(0.4))
                                .overlay(
                                    Text("BEFORE")
                                        .font(FixPixFonts.caption)
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 4)
                                        .background(Color.black.opacity(0.6))
                                        .foregroundColor(.white)
                                        .cornerRadius(4)
                                        .padding(12),
                                    alignment: .topLeading
                                )
                            
                            // After Side
                            Rectangle()
                                .fill(Color.clear)
                                .overlay(
                                    Text("AFTER")
                                        .font(FixPixFonts.caption)
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 4)
                                        .background(FixPixColors.fixpixGreen)
                                        .foregroundColor(.white)
                                        .cornerRadius(4)
                                        .padding(12),
                                    alignment: .topTrailing
                                )
                        }
                    )
                
                // Slider Handle (Static Visual)
                VStack(spacing: 0) {
                    Rectangle()
                        .fill(Color.white)
                        .frame(width: 2)
                    
                    Circle()
                        .fill(Color.white)
                        .frame(width: 40, height: 40)
                        .shadow(radius: 4)
                        .overlay(
                            HStack(spacing: 4) {
                                Image(systemName: "chevron.left")
                                Image(systemName: "chevron.right")
                            }
                            .font(.system(size: 10, weight: .bold))
                            .foregroundColor(FixPixColors.fixpixBrandBlue)
                        )
                    
                    Rectangle()
                        .fill(Color.white)
                        .frame(width: 2)
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: FixPixSpacing.cornerRadius))
            .padding(.horizontal, FixPixSpacing.horizontal)
            .shadow(color: FixPixColors.fixpixBrandBlue.opacity(0.2), radius: 20, x: 0, y: 10)
        }
    }
}

#Preview {
    ZStack {
        Color.black.edgesIgnoringSafeArea(.all)
        ComparisonCardView()
    }
}
