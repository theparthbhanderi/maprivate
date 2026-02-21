package com.fixpix.android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * FixPix Typography System - Production Grade
 * 
 * Based on iOS Human Interface Guidelines type scale
 * Using Inter font family (system fallback available)
 */

// =====================================================
// TYPE SCALE (Matching iOS HIG)
// =====================================================

@Immutable
data class FixPixTypographyScheme(
    // Large Title - Used for top headers
    val largeTitle: TextStyle,
    
    // Title 1 - Primary titles
    val title1: TextStyle,
    
    // Title 2 - Secondary titles
    val title2: TextStyle,
    
    // Title 3 - Tertiary titles
    val title3: TextStyle,
    
    // Headline - Bold callouts
    val headline: TextStyle,
    
    // Body - Standard text
    val body: TextStyle,
    
    // Callout - Slightly smaller than body
    val callout: TextStyle,
    
    // Subheadline - Section headers
    val subheadline: TextStyle,
    
    // Footnote - Small details
    val footnote: TextStyle,
    
    // Caption 1 - Labels
    val caption1: TextStyle,
    
    // Caption 2 - Smallest text
    val caption2: TextStyle
)

// Use system font (Inter-like on most devices)
private val FixPixFontFamily = FontFamily.Default

val FixPixTypography = FixPixTypographyScheme(
    largeTitle = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.37.sp
    ),
    title1 = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.36.sp
    ),
    title2 = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.35.sp
    ),
    title3 = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.38.sp
    ),
    headline = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    body = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    callout = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.32).sp
    ),
    subheadline = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.24).sp
    ),
    footnote = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.08).sp
    ),
    caption1 = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    caption2 = TextStyle(
        fontFamily = FixPixFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.07.sp
    )
)

val LocalFixPixTypography = staticCompositionLocalOf { FixPixTypography }
