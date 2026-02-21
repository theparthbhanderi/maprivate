package com.fixpix.ui.editor.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.fixpix.ui.editor.tools.AdjustmentTool
import com.fixpix.ui.editor.tools.EditorTool
import com.fixpix.ui.editor.tools.FilterNone
import com.fixpix.ui.editor.tools.FilterTool
import com.fixpix.ui.editor.tools.ObjectEraserTool
import com.fixpix.ui.editor.tools.ToolCategory
import com.fixpix.ui.editor.tools.Upscale1x
import com.fixpix.ui.editor.tools.UpscaleTool

enum class EditorMode {
    DEFAULT,
    MASKING,
    CROPPING,
    PROCESSED
}

class FixPixEditorState(
    initialImage: ImageBitmap? = null
) {
    // Images
    var currentImage by mutableStateOf(initialImage)
    var processedImage by mutableStateOf<ImageBitmap?>(null)
    var maskBitmap by mutableStateOf<ImageBitmap?>(null)

    // Mode
    var mode by mutableStateOf(EditorMode.DEFAULT)
        private set

    // Tool State
    var activeCategory by mutableStateOf(ToolCategory.ENHANCE)
    var activeTool by mutableStateOf<EditorTool?>(null)
    
    // Configuration
    // Using simple sets/maps to track active toggles and values
    val activeToggles = mutableStateListOf<String>() // For toggleable tools like FaceRestore
    val adjustments = mutableStateMapOf<String, Float>() // For sliders
    var activeFilter by mutableStateOf<FilterTool>(FilterNone)
    var activeUpscale by mutableStateOf<UpscaleTool>(Upscale1x)

    // UI State
    var isProcessing by mutableStateOf(false)
    var comparePosition by mutableStateOf(0.5f)
    var processingStep by mutableStateOf("") // "Analysing...", "Enhancing..."

    // --- Actions ---

    fun selectTool(tool: EditorTool) {
        if (isProcessing) return
        
        activeTool = tool
        
        // Handle specific tool behaviors
        when (tool) {
             ObjectEraserTool -> {
                 enterMode(EditorMode.MASKING)
             }
             is FilterTool -> {
                 activeFilter = tool
                 // Filters usually auto-apply or specific logic
             }
             else -> {
                 // For standard tools, we just set activeTool. 
                 // If it was masking, we might need to exit?
                 if (mode == EditorMode.MASKING) exitMode()
             }
        }
    }

    fun toggleOption(toolId: String) {
        if (activeToggles.contains(toolId)) {
            activeToggles.remove(toolId)
        } else {
            activeToggles.add(toolId)
        }
    }

    fun setAdjustmentValue(toolId: String, value: Float) {
        adjustments[toolId] = value
    }

    fun setUpscaleOption(tool: UpscaleTool) {
        activeUpscale = tool
    }

    fun enterMode(newMode: EditorMode) {
        mode = newMode
    }

    fun exitMode() {
        mode = EditorMode.DEFAULT
        // Reset specific mode data if needed, e.g. clear mask if cancelled?
    }
    
    fun setProcessing(processing: Boolean, step: String = "") {
        isProcessing = processing
        processingStep = step
    }

    fun getAdjustmentValue(tool: AdjustmentTool): Float {
        return adjustments[tool.id] ?: tool.default
    }
}
