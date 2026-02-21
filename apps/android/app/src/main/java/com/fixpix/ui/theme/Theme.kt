package com.fixpix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Map FixPix Colors to Material 3 ColorScheme for compatibility where needed
// However, our custom components will primarily use LocalFixPixColors

private val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    secondary = GreenDark,
    tertiary = PurpleDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = RedDark,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onTertiary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onError = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = BlueLight,
    secondary = GreenLight,
    tertiary = PurpleLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = RedLight,
    onPrimary = TextPrimaryLight,
    onSecondary = TextPrimaryLight, // White text on dark green? iOS standard is usually white text on filled buttons.
    onTertiary = TextPrimaryLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onError = TextPrimaryLight
)

@Composable
fun FixPixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but we want to ENFORCE FixPix/iOS brand
    // so we default it to FALSE to maintain the audit's strict color system.
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val fixPixColors = if (darkTheme) DarkColors else LightColors
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar color should be transparent or background to let content flow?
            // iOS usually matches background or is transparent.
            window.statusBarColor = fixPixColors.background.toArgb()
            window.navigationBarColor = fixPixColors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalFixPixColors provides fixPixColors,
        LocalFixPixTypography provides DefaultTypography,
        LocalFixPixShapes provides FixPixDefaultShapes,
        LocalFixPixDimens provides DefaultDimens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            // We don't map typography/shapes to MaterialTheme directly because the structure differs significantly.
            // Custom components should use FixPixTheme.typography etc.
            content = content
        )
    }
}

// Accessors
object FixPixTheme {
    val colors: FixPixColors
        @Composable
        get() = LocalFixPixColors.current
        
    val typography: FixPixTypography
        @Composable
        get() = LocalFixPixTypography.current
        
    val shapes: FixPixShapes
        @Composable
        get() = LocalFixPixShapes.current
        
    val dimens: FixPixDimens
        @Composable
        get() = LocalFixPixDimens.current
}
