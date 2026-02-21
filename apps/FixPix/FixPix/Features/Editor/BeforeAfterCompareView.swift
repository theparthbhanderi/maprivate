//
//  BeforeAfterCompareView.swift
//  FixPix
//

import SwiftUI

struct BeforeAfterCompareView: View {
    let originalImage: UIImage
    let editedImage: UIImage
    
    @StateObject private var viewModel = CompareViewModel()
    
    var body: some View {
        GeometryReader { geometry in
            let size = geometry.size
            
            ZStack {
                // Background studio layer
                FixPixColors.fixpixDarkBg.ignoresSafeArea()
                
                // 1. Original Image (Before)
                Image(uiImage: originalImage)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: size.width * 0.9, height: size.height * 0.9)
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
                
                // 2. Edited Image (After) with Mask
                Image(uiImage: editedImage)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: size.width * 0.9, height: size.height * 0.9)
                    .mask(
                        HStack(spacing: 0) {
                            Spacer()
                                .frame(width: size.width * viewModel.sliderPosition)
                                .background(Color.clear)
                            
                            Rectangle()
                                .fill(Color.white)
                        }
                    )
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
                
                // 3. Draggable Slider Handle
                HStack(spacing: 0) {
                    Spacer()
                        .frame(width: size.width * viewModel.sliderPosition)
                    
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
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                let position = value.location.x / size.width
                                viewModel.sliderPosition = min(max(position, 0), 1)
                            }
                    )
                    
                    Spacer()
                }
            }
        }
    }
}

#Preview {
    let mock = UIImage(systemName: "photo.fill")!
    BeforeAfterCompareView(originalImage: mock, editedImage: mock)
        .preferredColorScheme(.dark)
}
