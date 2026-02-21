package com.fixpix.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun FixPixSurface(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.default,
    color: Color = FixPixTheme.colors.surface,
    contentColor: Color = FixPixTheme.colors.textPrimary,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        shadowElevation = elevation,
        content = content
    )
}

/**
 * A specialized surface for Cards that handles the subtle differences between Light/Dark mode.
 * Light Mode: Uses shadow (elevation).
 * Dark Mode: Uses a thin border (separator) instead of shadow, for iOS consistency.
 */
@Composable
fun FixPixCard(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.xl, // Default 16dp
    backgroundColor: Color = FixPixTheme.colors.surface,
    elevation: Dp = 2.dp, // Default iOS 'default' shadow approximation
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val isDark = FixPixTheme.colors.isDark
    
    val shadowModifier = if (!isDark && elevation > 0.dp) {
        Modifier.shadow(elevation, shape, clip = false)
    } else {
        Modifier
    }
    
    val borderModifier = if (isDark) {
        Modifier.border(
            width = 0.5.dp, // Hairline
            color = FixPixTheme.colors.separator,
            shape = shape
        )
    } else {
        Modifier
    }
    
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier
            .then(shadowModifier)
            .then(borderModifier),
        shape = shape,
        color = backgroundColor,
        contentColor = FixPixTheme.colors.textPrimary,
        // We handle shadow manually via modifier for conditional logic, so set Surface elevation to 0
        shadowElevation = 0.dp 
    ) {
        content()
    }
}

/**
 * Stimulates the glassmorphism effect. 
 * Note: Realtime blur is expensive/complex on Android versions < 12. 
 * We fallback to a semi-transparent surface with a subtle border.
 */
@Composable
fun FixPixGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.default,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(FixPixTheme.colors.surface.copy(alpha = 0.75f))
            .border(
                width = 0.5.dp,
                color = FixPixTheme.colors.separator.copy(alpha = 0.2f),
                shape = shape
            )
    ) {
        content()
    }
}
