package com.fixpix.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Base Colors (iOS System)
val BlueLight = Color(0xFF007AFF)
val BlueDark = Color(0xFF0A84FF)
val GreenLight = Color(0xFF34C759)
val GreenDark = Color(0xFF30D158)
val RedLight = Color(0xFFFF3B30)
val RedDark = Color(0xFFFF453A)
val OrangeLight = Color(0xFFFF9500)
val OrangeDark = Color(0xFFFF9F0A)
val PurpleLight = Color(0xFFAF52DE)
val PurpleDark = Color(0xFFBF5AF2)
val TealLight = Color(0xFF5AC8FA)
val TealDark = Color(0xFF64D2FF)
val IndigoLight = Color(0xFF5856D6)
val IndigoDark = Color(0xFF5E5CE6)

// Semantic Colors
val BackgroundLight = Color(0xFFF2F2F7)
val BackgroundDark = Color(0xFF000000)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1C1C1E)
val SurfaceSecondaryLight = Color(0xFFF2F2F7)
val SurfaceSecondaryDark = Color(0xFF2C2C2E)
val SeparatorLight = Color(0xFF3C3C43) // rgb(60, 60, 67)
val SeparatorDark = Color(0xFF545458) // rgb(84, 84, 88)

// Text Colors (iOS Standard)
val TextPrimaryLight = Color(0xFF000000)
val TextPrimaryDark = Color(0xFFFFFFFF)
val TextSecondaryLight = Color(0xFF8A8A8E) // equivalent to systemGray2 or label.secondary
val TextSecondaryDark = Color(0xFF8E8E93)
val TextTertiaryLight = Color(0xFFC7C7CC)
val TextTertiaryDark = Color(0xFF48484A)

data class FixPixColors(
    val primary: Color,
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val separator: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val success: Color,
    val error: Color,
    val warning: Color,
    val isDark: Boolean
)

val LightColors = FixPixColors(
    primary = BlueLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceSecondary = SurfaceSecondaryLight,
    separator = SeparatorLight.copy(alpha = 0.29f), // Adjusted alpha standard for iOS lines
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    success = GreenLight,
    error = RedLight,
    warning = OrangeLight,
    isDark = false
)

val DarkColors = FixPixColors(
    primary = BlueDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceSecondary = SurfaceSecondaryDark,
    separator = SeparatorDark.copy(alpha = 0.6f),
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    success = GreenDark,
    error = RedDark,
    warning = OrangeDark,
    isDark = true
)

val LocalFixPixColors = staticCompositionLocalOf { LightColors }
