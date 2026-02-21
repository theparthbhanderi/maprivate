package com.fixpix.ui.editor.state

import com.fixpix.data.model.ProcessingSettings

/**
 * Maps the UI Editor State to the Backend Processing Settings DTO.
 */
fun FixPixEditorState.toProcessingSettings(): ProcessingSettings {
    // Basic Settings
    val settings = ProcessingSettings(
        removeScratches = this.enhanceRestoration,
        faceRestoration = this.enhanceFace,
        colorize = this.enhanceColorize,
        upscaleX = if (this.enhanceUpscale) 2 else 1,
        removeBackground = this.magicRemoveBg,
        filterPreset = this.activeFilter
    )
    
    // Add Adjustments
    // Note: DTO uses nullable properties, we can create a copy with adjustments
    // Since Kotlin data classes effectively are immutable, we construct it fully here.
    
    return settings.copy(
        brightness = this.adjustments["brightness"],
        contrast = this.adjustments["contrast"],
        saturation = this.adjustments["saturation"],
        // Add others if backend supports them (e.g. warmth/sharpness mapping needed in future)
    )
}

/**
 * Helper to get Mask Base64 if exists.
 * In a real app, this would perform Bitmap -> Base64 conversion.
 */
fun FixPixEditorState.getMaskBase64(): String? {
    // Placeholder: Return null or mock string
    // Implementing Bitmap capture from Canvas paths requires view-context or drawing to a software bitmap
    return if (this.maskPaths.isNotEmpty()) "data:image/png;base64,..." else null
}
