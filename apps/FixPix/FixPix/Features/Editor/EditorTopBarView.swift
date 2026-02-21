//
//  EditorTopBarView.swift
//  FixPix
//

import SwiftUI

struct EditorTopBarView: View {
    @ObservedObject var viewModel: EditorViewModel
    let onBack: () -> Void
    let onExport: () -> Void
    
    var body: some View {
        HStack {
            // Back
            Button(action: onBack) {
                Image(systemName: "chevron.left")
                    .font(.title3.bold())
                    .foregroundColor(FixPixColors.text)
            }
            
            Spacer()
            
            // Undo / Redo Group
            HStack(spacing: 20) {
                Button(action: { viewModel.undo() }) {
                    Image(systemName: "arrow.uturn.backward")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(viewModel.canUndo ? FixPixColors.text : FixPixColors.secondaryText)
                }
                .disabled(!viewModel.canUndo)
                
                Button(action: { viewModel.redo() }) {
                    Image(systemName: "arrow.uturn.forward")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(viewModel.canRedo ? FixPixColors.text : FixPixColors.secondaryText)
                }
                .disabled(!viewModel.canRedo)
                
                // Compare Button
                Button(action: { viewModel.toggleComparison() }) {
                    Image(systemName: viewModel.isShowingComparison ? "square.rightthird.inset.filled" : "square.split.2x1")
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(viewModel.isShowingComparison ? FixPixColors.fixpixBrandBlue : FixPixColors.text)
                }
            }
            
            Spacer()
            
            // Export
            Button(action: onExport) {
                HStack(spacing: 6) {
                    Text("Export")
                        .font(FixPixFonts.button())
                    Image(systemName: "arrow.up.square")
                        .font(.system(size: 16, weight: .bold))
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(FixPixColors.fixpixBrandBlue)
                .foregroundColor(.white)
                .cornerRadius(FixPixSpacing.buttonRadius)
            }
        }
        .padding(.horizontal, FixPixSpacing.horizontal)
        .padding(.vertical, 12)
        .background(FixPixColors.background.opacity(0.8))
        .background(BlurView(style: .systemThinMaterial))
    }
}
