package com.fixpix.ui.theme

import androidx.compose.ui.graphics.Color

// iOS System Colors (from Tailwind Config)
object FixPixColors {
    // Primary Blue
    val Blue = Color(0xFF007AFF)
    val BlueDark = Color(0xFF0A84FF)
    
    // Semantic Colors
    val Green = Color(0xFF34C759)
    val Red = Color(0xFFFF3B30)
    val Orange = Color(0xFFFF9500)
    val Teal = Color(0xFF5AC8FA)
    val Purple = Color(0xFFAF52DE)
    
    // Grays (System Gray 1-6)
    val SystemGray = Color(0xFF8E8E93)
    val SystemGray2 = Color(0xFFAEAEB2)
    val SystemGray3 = Color(0xFFC7C7CC)
    val SystemGray4 = Color(0xFFD1D1D6)
    val SystemGray5 = Color(0xFFE5E5EA)
    val SystemGray6 = Color(0xFFF2F2F7)
    
    // Dark Mode Grays
    val SystemGrayDark = Color(0xFF8E8E93)
    val SystemGray2Dark = Color(0xFF636366)
    val SystemGray3Dark = Color(0xFF48484A)
    val SystemGray4Dark = Color(0xFF3A3A3C)
    val SystemGray5Dark = Color(0xFF2C2C2E)
    val SystemGray6Dark = Color(0xFF1C1C1E)

    // Backgrounds
    val BackgroundLight = Color(0xFFF2F2F7) // iOS System Grouped Background
    val BackgroundDark = Color(0xFF000000)
    
    val SurfaceLight = Color(0xFFFFFFFF)
    val SurfaceDark = Color(0xFF1C1C1E) // iOS Elevated Surface

    // Text
    val LabelPrimaryLight = Color(0xFF000000) // 100%
    val LabelSecondaryLight = Color(0x993C3C43) // 60%
    val LabelTertiaryLight = Color(0x4D3C3C43) // 30%
    
    val LabelPrimaryDark = Color(0xFFFFFFFF)
    val LabelSecondaryDark = Color(0x99EBEBF5)
    val LabelTertiaryDark = Color(0x4DEBEBF5)
    
    // Separators
    val SeparatorLight = Color(0x4A3C3C43) // ~29%
    val SeparatorDark = Color(0x99545458) // ~60%
}
