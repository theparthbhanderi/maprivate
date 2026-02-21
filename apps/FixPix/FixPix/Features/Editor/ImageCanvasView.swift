//
//  ImageCanvasView.swift
//  FixPix
//

import SwiftUI

struct ImageCanvasView: View {
    @ObservedObject var viewModel: EditorViewModel
    
    var body: some View {
        ZStack {
            // Background studio layer
            FixPixColors.fixpixDarkBg.ignoresSafeArea()
            
            // Dot Grid Background
            Canvas { context, size in
                let step: CGFloat = 30
                for x in stride(from: 0, through: size.width, by: step) {
                    for y in stride(from: 0, through: size.height, by: step) {
                        let rect = CGRect(x: x, y: y, width: 2, height: 2)
                        context.fill(Path(ellipseIn: rect), with: .color(Color.white.opacity(0.1)))
                    }
                }
            }
            .ignoresSafeArea()
            
            // Image Preview Container
            Group {
                if let image = viewModel.selectedImage {
                   Image(uiImage: image)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .cornerRadius(8)
                        .shadow(color: .black.opacity(0.3), radius: 20, x: 0, y: 10)
                } else {
                    // Placeholder
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color.gray.opacity(0.2))
                        .frame(width: 250, height: 350)
                        .overlay(
                            VStack(spacing: 12) {
                                Image(systemName: "photo.on.rectangle.angled")
                                    .font(.system(size: 40))
                                Text("No Image Loaded")
                                    .font(FixPixFonts.body)
                            }
                            .foregroundColor(FixPixColors.secondaryText)
                        )
                }
            }
            .scaleEffect(viewModel.scale)
            .offset(viewModel.offset)
            .gesture(transformGesture)
            

        }
        .clipped()
    }
    
    private var transformGesture: some Gesture {
        SimultaneousGesture(
            MagnificationGesture()
                .onChanged { value in
                    let delta = value / viewModel.lastScale
                    viewModel.lastScale = value
                    viewModel.scale *= delta
                }
                .onEnded { _ in
                    viewModel.lastScale = 1.0
                },
            DragGesture()
                .onChanged { value in
                    let delta = CGSize(
                        width: value.translation.width - viewModel.lastOffset.width,
                        height: value.translation.height - viewModel.lastOffset.height
                    )
                    viewModel.lastOffset = value.translation
                    viewModel.offset.width += delta.width
                    viewModel.offset.height += delta.height
                }
                .onEnded { _ in
                    viewModel.lastOffset = .zero
                }
        )
    }
}
