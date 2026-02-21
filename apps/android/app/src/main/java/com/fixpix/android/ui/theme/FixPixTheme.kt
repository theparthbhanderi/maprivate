package com.fixpix.android.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * FixPix Theme - Production Grade Theme API
 * 
 * Central theme provider for the entire FixPix Android application.
 * All UI components must use this theme for consistency.
 */

@Composable
fun FixPixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    CompositionLocalProvider(
        LocalFixPixColors provides colorScheme,
        LocalFixPixTypography provides FixPixTypography,
        LocalFixPixShapes provides FixPixShapes,
        LocalFixPixSpacing provides FixPixSpacing,
        LocalFixPixDimens provides FixPixDimens,
        LocalFixPixMotion provides FixPixMotion,
        content = content
    )
}

/**
 * Theme accessor object for convenient access to design tokens
 * 
 * Usage:
 * - FixPixTheme.colors.primary
 * - FixPixTheme.typography.title1
 * - FixPixTheme.shapes.large
 * - FixPixTheme.spacing.lg
 * - FixPixTheme.dimens.buttonHeightMedium
 * - FixPixTheme.motion.springPress
 */
object FixPixTheme {
    val colors: FixPixColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixColors.current
    
    val typography: FixPixTypographyScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixTypography.current
    
    val shapes: FixPixShapeScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixShapes.current
    
    val spacing: FixPixSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixSpacing.current
    
    val dimens: FixPixDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixDimens.current
    
    val motion: FixPixMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalFixPixMotion.current
}
