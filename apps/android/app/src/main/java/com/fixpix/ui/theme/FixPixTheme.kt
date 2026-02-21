package com.fixpix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FixPixColors.BlueDark,
    secondary = FixPixColors.Purple,
    tertiary = FixPixColors.Teal,
    background = FixPixColors.BackgroundDark,
    surface = FixPixColors.SurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = FixPixColors.LabelPrimaryDark,
    onSurface = FixPixColors.LabelPrimaryDark,
)

private val LightColorScheme = lightColorScheme(
    primary = FixPixColors.Blue,
    secondary = FixPixColors.Purple,
    tertiary = FixPixColors.Teal,
    background = FixPixColors.BackgroundLight,
    surface = FixPixColors.SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = FixPixColors.LabelPrimaryLight,
    onSurface = FixPixColors.LabelPrimaryLight,
)

@Composable
fun FixPixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // We default to FALSE to strictly enforce our iOS Design System
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Use transparent status bar for edge-to-edge
            window.statusBarColor = Color.Transparent.toArgb() 
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // We still wrap MaterialTheme to provide defaults for Material components that we might barely use
    // But our custom components will rely on our own tokens eventually
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography, // We'll override specific styles or use our object
        content = content
    )
}
