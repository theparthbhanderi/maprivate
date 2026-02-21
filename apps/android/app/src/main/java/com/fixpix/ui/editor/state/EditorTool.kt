package com.fixpix.ui.editor.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the main categories of tools in the editor.
 */
enum class ToolCategory(val label: String, val icon: ImageVector) {
    ENHANCE("Enhance", Icons.Default.AutoFixHigh),
    MAGIC("Magic", Icons.Default.Brush),
    ADJUST("Adjust", Icons.Default.Tune),
    FILTERS("Filters", Icons.Default.ColorLens)
}

/**
 * Specific tools available within categories.
 */
sealed class EditorTool(
    val id: String,
    val label: String,
    val category: ToolCategory,
    val icon: ImageVector? = null,
    val description: String = ""
) {
    // Enhance Tools
    object Restore : EditorTool("restore", "Restore", ToolCategory.ENHANCE, description = "Remove scratches and damage")
    object FaceEnhance : EditorTool("face_enhance", "Face Enhance", ToolCategory.ENHANCE, Icons.Default.Face, "Clarify facial details")
    object Colorize : EditorTool("colorize", "Colorize", ToolCategory.ENHANCE, description = "Add color to B&W photos")
    object Upscale : EditorTool("upscale", "Upscale", ToolCategory.ENHANCE, description = "Increase resolution")

    // Magic Tools
    object RemoveBackground : EditorTool("remove_bg", "Remove BG", ToolCategory.MAGIC, description = "Transparent background")
    object ObjectEraser : EditorTool("eraser", "Eraser", ToolCategory.MAGIC, description = "Paint to remove objects")

    // Adjustments (Value based)
    data class Adjustment(
        val typeId: String,
        val name: String,
        val min: Float = 0f,
        val max: Float = 2f,
        val default: Float = 1f
    ) : EditorTool(typeId, name, ToolCategory.ADJUST)

    // Filters (Preset based)
    object Filter : EditorTool("filter_root", "Filters", ToolCategory.FILTERS)
}

// Static list of Adjustments
val AdjustmentTools = listOf(
    EditorTool.Adjustment("brightness", "Brightness", 0.5f, 1.5f, 1.0f),
    EditorTool.Adjustment("contrast", "Contrast", 0.5f, 1.5f, 1.0f),
    EditorTool.Adjustment("saturation", "Saturation", 0f, 2f, 1.0f),
    EditorTool.Adjustment("warmth", "Warmth", -1f, 1f, 0f),
    EditorTool.Adjustment("sharpness", "Sharpness", 0f, 2f, 0f) // 0 means no sharpen
)

// Static list of Filters
val FilterPresets = listOf(
    "Vintage", "Cinematic", "B&W Classic", "B&W Noir", "Cool", "Warm", "Vivid", "Fade"
)
