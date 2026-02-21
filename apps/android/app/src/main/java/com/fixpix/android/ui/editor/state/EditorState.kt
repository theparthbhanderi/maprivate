package com.fixpix.android.ui.editor.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path

// Core Editor Modes
enum class EditorMode {
    EMPTY,
    DEFAULT,
    MASKING,
    CROPPING,
    PROCESSED
}

// Processing Steps
enum class ProcessingStep {
    IDLE,
    ANALYZING,
    ENHANCING,
    FINALIZING
}

// Full Settings Contract (Matches Web JSON)
// @Serializable -- Uncomment when backend integration is ready
data class EditorSettings(
    // Enhance
    val removeScratches: Boolean = false,
    val faceRestoration: Boolean = false,
    val colorize: Boolean = false,
    val upscaleX: Int = 1, // 1, 2, 4
    
    // Magic
    val autoEnhance: Boolean = false,
    val removeBackground: Boolean = false,
    // Object Eraser is an action that generates a mask, not just a setting, 
    // but ultimately triggers inpainting.
    
    // Adjust
    val brightness: Float = 1.0f, // 0.5 - 1.5
    val contrast: Float = 1.0f,   // 0.5 - 1.5
    val saturation: Float = 1.0f, // 0.0 - 2.0
    val whiteBalance: Boolean = false,
    
    // Filters
    val filterPreset: String = "none",
    val denoiseStrength: Int = 0
)

@Immutable
data class EditorState(
    val originalImageUri: Uri? = null,
    val processedImageUri: Uri? = null,
    val mode: EditorMode = EditorMode.EMPTY,
    val settings: EditorSettings = EditorSettings(),
    val isProcessing: Boolean = false,
    val processingStep: ProcessingStep = ProcessingStep.IDLE,
    
    // Masking State
    val maskPaths: List<Path> = emptyList(), // For drawing
    val currentBrushSize: Float = 40f,
    
    // Comparison State
    val showComparison: Boolean = false,
    val comparisonPosition: Float = 0.5f,
    
    // History
    val canUndo: Boolean = false,
    val canRedo: Boolean = false
)
