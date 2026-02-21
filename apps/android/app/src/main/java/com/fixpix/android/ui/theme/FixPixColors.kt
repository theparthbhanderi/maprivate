package com.fixpix.android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * FixPix Color System - Production Grade
 * 
 * Extracted from FixPix website design tokens
 * Supports Light and Dark modes with semantic color roles
 */

// =====================================================
// RAW PALETTE - iOS System Colors
// =====================================================

object FixPixPalette {
    // Primary Blue
    val Blue = Color(0xFF007AFF)
    val BlueDark = Color(0xFF0A84FF)
    
    // Secondary
    val Green = Color(0xFF34C759)
    val GreenDark = Color(0xFF30D158)
    val Orange = Color(0xFFFF9500)
    val OrangeDark = Color(0xFFFF9F0A)
    val Red = Color(0xFFFF3B30)
    val RedDark = Color(0xFFFF453A)
    val Purple = Color(0xFFAF52DE)
    val PurpleDark = Color(0xFFBF5AF2)
    val Teal = Color(0xFF5AC8FA)
    val TealDark = Color(0xFF64D2FF)
    
    // Grays (Light Mode)
    val Gray = Color(0xFF8E8E93)
    val Gray2 = Color(0xFFAEAEB2)
    val Gray3 = Color(0xFFC7C7CC)
    val Gray4 = Color(0xFFD1D1D6)
    val Gray5 = Color(0xFFE5E5EA)
    val Gray6 = Color(0xFFF2F2F7)
    
    // Grays (Dark Mode)
    val GrayDark = Color(0xFF8E8E93)
    val Gray2Dark = Color(0xFF636366)
    val Gray3Dark = Color(0xFF48484A)
    val Gray4Dark = Color(0xFF3A3A3C)
    val Gray5Dark = Color(0xFF2C2C2E)
    val Gray6Dark = Color(0xFF1C1C1E)
    
    // Pure
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
}

// =====================================================
// SEMANTIC COLORS
// =====================================================

@Immutable
data class FixPixColorScheme(
    // Primary
    val primary: Color,
    val onPrimary: Color,
    
    // Background & Surface
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val surfaceTertiary: Color,
    
    // Text
    val textMain: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    
    // Semantic
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,
    
    // System
    val fill: Color,
    val separator: Color,
    val overlay: Color,
    
    // Editor Specific
    val canvasBackground: Color,
    
    // Gradients start/end colors
    val gradientPrimaryStart: Color,
    val gradientPrimaryEnd: Color
)

val LightColorScheme = FixPixColorScheme(
    // Primary
    primary = FixPixPalette.Blue,
    onPrimary = FixPixPalette.White,
    
    // Background & Surface
    background = FixPixPalette.Gray6,           // #F2F2F7
    surface = FixPixPalette.White,
    surfaceSecondary = Color(0xFFF9F9F9),
    surfaceTertiary = Color(0xFFEFEFF4),
    
    // Text
    textMain = FixPixPalette.Black,
    textSecondary = Color(0xFF3C3C43).copy(alpha = 0.6f),
    textTertiary = Color(0xFF3C3C43).copy(alpha = 0.3f),
    
    // Semantic
    success = FixPixPalette.Green,
    warning = FixPixPalette.Orange,
    error = FixPixPalette.Red,
    info = FixPixPalette.Teal,
    
    // System
    fill = Color(0xFF787880).copy(alpha = 0.2f),
    separator = Color(0xFF3C3C43).copy(alpha = 0.2f),
    overlay = FixPixPalette.Black.copy(alpha = 0.4f),
    
    // Editor
    canvasBackground = Color(0xFF0A0A0C),
    
    // Gradients
    gradientPrimaryStart = FixPixPalette.Blue,
    gradientPrimaryEnd = FixPixPalette.Purple
)

val DarkColorScheme = FixPixColorScheme(
    // Primary
    primary = FixPixPalette.BlueDark,
    onPrimary = FixPixPalette.White,
    
    // Background & Surface
    background = FixPixPalette.Black,
    surface = FixPixPalette.Gray6Dark,          // #1C1C1E
    surfaceSecondary = FixPixPalette.Gray5Dark,
    surfaceTertiary = FixPixPalette.Gray4Dark,
    
    // Text
    textMain = FixPixPalette.White,
    textSecondary = Color(0xFFEBEBF5).copy(alpha = 0.6f),
    textTertiary = Color(0xFFEBEBF5).copy(alpha = 0.3f),
    
    // Semantic
    success = FixPixPalette.GreenDark,
    warning = FixPixPalette.OrangeDark,
    error = FixPixPalette.RedDark,
    info = FixPixPalette.TealDark,
    
    // System
    fill = Color(0xFF787880).copy(alpha = 0.36f),
    separator = Color(0xFF545458).copy(alpha = 0.6f),
    overlay = FixPixPalette.Black.copy(alpha = 0.6f),
    
    // Editor
    canvasBackground = Color(0xFF0A0A0C),
    
    // Gradients
    gradientPrimaryStart = FixPixPalette.BlueDark,
    gradientPrimaryEnd = FixPixPalette.PurpleDark
)

val LocalFixPixColors = staticCompositionLocalOf { LightColorScheme }
