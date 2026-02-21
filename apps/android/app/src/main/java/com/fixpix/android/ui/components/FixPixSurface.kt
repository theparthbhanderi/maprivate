package com.fixpix.android.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun FixPixSurface(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.medium,
    color: Color = FixPixTheme.colors.surface,
    contentColor: Color = FixPixTheme.colors.textMain,
    border: Border? = null,
    elevation: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .then(if (elevation > 0.dp) Modifier.shadow(elevation, shape) else Modifier)
            .clip(shape)
            .then(if (border != null) Modifier.border(border.width, border.color, shape) else Modifier)
            .background(color, shape)
    ) {
        content()
    }
}

data class Border(val width: Dp, val color: Color)

@Composable
fun FixPixGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.extraLarge,
    content: @Composable BoxScope.() -> Unit
) {
    // Glassmorphism implementation
    // Ideally we would use RenderEffect for blur on Android 12+, but for pure Compose setup
    // without View Interop complexity, we use a high-alpha scrim.
    // Spec: backdrop-filter: blur(40px) saturate(180%)
    
    val glassColor = FixPixTheme.colors.surface.copy(alpha = 0.85f)
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(glassColor)
    ) {
        content()
    }
}


