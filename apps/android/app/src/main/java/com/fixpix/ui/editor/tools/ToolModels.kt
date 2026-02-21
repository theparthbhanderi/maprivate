package com.fixpix.ui.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolCategory(val label: String) {
    ENHANCE("Enhance"),
    MAGIC("Magic"),
    ADJUST("Adjust"),
    FILTERS("Filters")
}

sealed class EditorTool(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val category: ToolCategory
)

// --- Enhance Tools ---
object FaceRestoreTool : EditorTool("face_restore", "Face Restore", Icons.Default.Face, ToolCategory.ENHANCE)
object ScratchRemovalTool : EditorTool("scratch_removal", "Scratch Removal", Icons.Default.AutoFixHigh, ToolCategory.ENHANCE)
object ColorizeTool : EditorTool("colorize", "Colorize", Icons.Default.ColorLens, ToolCategory.ENHANCE)

sealed class UpscaleTool(val scaleFactor: Int) : EditorTool("upscale_${scaleFactor}x", "${scaleFactor}x Upscale", Icons.Default.HighQuality, ToolCategory.ENHANCE)
object Upscale1x : UpscaleTool(1)
object Upscale2x : UpscaleTool(2)
object Upscale4x : UpscaleTool(4)

// --- Magic Tools ---
object AutoEnhanceTool : EditorTool("auto_enhance", "Auto Enhance", Icons.Default.AutoFixHigh, ToolCategory.MAGIC)
object RemoveBackgroundTool : EditorTool("remove_bg", "Remove Bg", Icons.Default.ContentCut, ToolCategory.MAGIC)
object ObjectEraserTool : EditorTool("object_eraser", "Eraser", Icons.Default.Brush, ToolCategory.MAGIC)

// --- Adjust Tools ---
sealed class AdjustmentTool(
    id: String,
    label: String,
    icon: ImageVector,
    val min: Float,
    val max: Float,
    val default: Float
) : EditorTool(id, label, icon, ToolCategory.ADJUST)

object BrightnessTool : AdjustmentTool("brightness", "Brightness", Icons.Default.LightMode, 0.5f, 1.5f, 1.0f)
object ContrastTool : AdjustmentTool("contrast", "Contrast", Icons.Default.Contrast, 0.5f, 1.5f, 1.0f)
object SaturationTool : AdjustmentTool("saturation", "Saturation", Icons.Default.ColorLens, 0.0f, 2.0f, 1.0f)

// --- Filter Tools ---
sealed class FilterTool(id: String, label: String) : EditorTool(id, label, Icons.Default.Filter, ToolCategory.FILTERS)
object FilterNone : FilterTool("filter_none", "None")
object FilterVintage : FilterTool("filter_vintage", "Vintage")
object FilterCinematic : FilterTool("filter_cinematic", "Cinematic")
object FilterWarm : FilterTool("filter_warm", "Warm")
object FilterCool : FilterTool("filter_cool", "Cool")
object FilterBW : FilterTool("filter_bw", "B&W")
object FilterFade : FilterTool("filter_fade", "Fade")
object FilterVivid : FilterTool("filter_vivid", "Vivid")

// Grouping for UI
val AdjustmentTools = listOf(BrightnessTool, ContrastTool, SaturationTool)
val FilterTools = listOf(FilterNone, FilterVintage, FilterCinematic, FilterWarm, FilterCool, FilterBW, FilterFade, FilterVivid)
val EnhanceTools = listOf(FaceRestoreTool, ScratchRemovalTool, ColorizeTool, Upscale1x, Upscale2x, Upscale4x)
val MagicTools = listOf(AutoEnhanceTool, RemoveBackgroundTool, ObjectEraserTool)

val AllTools = EnhanceTools + MagicTools + AdjustmentTools + FilterTools
