package com.fixpix.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class FixPixDimens(
    val spacing2xs: Dp = 4.dp,
    val spacingXs: Dp = 8.dp,
    val spacingSm: Dp = 12.dp,
    val spacingMd: Dp = 16.dp, // Gutter
    val spacingLg: Dp = 20.dp,
    val spacingXl: Dp = 24.dp,
    val spacing2xl: Dp = 32.dp,
    val spacing3xl: Dp = 40.dp,
    
    // Specific Component Dimens
    val buttonHeightSm: Dp = 36.dp,
    val buttonHeightMd: Dp = 44.dp,
    val buttonHeightLg: Dp = 50.dp,
    
    val inputHeight: Dp = 50.dp,
    val toggleWidthDefault: Dp = 51.dp,
    val toggleHeightDefault: Dp = 31.dp,
    val toggleWidthSmall: Dp = 44.dp,
    val toggleHeightSmall: Dp = 26.dp
)

val DefaultDimens = FixPixDimens()

val LocalFixPixDimens = staticCompositionLocalOf { DefaultDimens }
