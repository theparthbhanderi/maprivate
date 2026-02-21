package com.fixpix.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * FixPix Shapes - Production Grade
 * 
 * Corner radius scale matching iOS design language
 */

@Immutable
data class FixPixShapeScheme(
    val none: RoundedCornerShape,
    val extraSmall: RoundedCornerShape,  // 4dp - Pills, badges
    val small: RoundedCornerShape,        // 8dp - Chips, small cards
    val medium: RoundedCornerShape,       // 12dp - Standard cards
    val large: RoundedCornerShape,        // 16dp - Large cards, buttons
    val extraLarge: RoundedCornerShape,   // 20dp - Sheets
    val full: RoundedCornerShape          // 50% - Circles, pills
)

val FixPixShapes = FixPixShapeScheme(
    none = RoundedCornerShape(0.dp),
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
    full = RoundedCornerShape(50)
)

val LocalFixPixShapes = staticCompositionLocalOf { FixPixShapes }

/**
 * FixPix Spacing - Production Grade
 * 
 * 4dp base unit spacing scale
 */

@Immutable
object FixPixSpacing {
    val none: Dp = 0.dp
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val xxxl: Dp = 32.dp
    val huge: Dp = 48.dp
    val massive: Dp = 64.dp
}

val LocalFixPixSpacing = staticCompositionLocalOf { FixPixSpacing }

/**
 * FixPix Dimensions - Component sizes
 */

@Immutable
object FixPixDimens {
    // Button heights
    val buttonHeightSmall: Dp = 32.dp
    val buttonHeightMedium: Dp = 44.dp
    val buttonHeightLarge: Dp = 56.dp
    
    // Icon sizes
    val iconSmall: Dp = 16.dp
    val iconMedium: Dp = 24.dp
    val iconLarge: Dp = 32.dp
    val iconHuge: Dp = 48.dp
    
    // Touch targets
    val minTouchTarget: Dp = 44.dp
    
    // Navigation
    val topBarHeight: Dp = 56.dp
    val bottomNavHeight: Dp = 83.dp
    
    // Cards
    val cardMinHeight: Dp = 88.dp
    val statCardHeight: Dp = 120.dp
    
    // Sheet
    val sheetHandleWidth: Dp = 36.dp
    val sheetHandleHeight: Dp = 4.dp
}

val LocalFixPixDimens = staticCompositionLocalOf { FixPixDimens }
