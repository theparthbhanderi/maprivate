package com.fixpix.android.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun FixPixCard(
    modifier: Modifier = Modifier,
    shape: Shape = FixPixTheme.shapes.extraLarge, // 16dp standard for iOS styled cards
    backgroundColor: Color = FixPixTheme.colors.surface,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    // Light mode: Shadow
    // Dark mode: Border
    
    val shadowElevation = if (isDark) 0.dp else 2.dp
    val borderModifier = if (isDark) {
        Modifier.border(1.dp, FixPixTheme.colors.separator.copy(alpha = 0.15f), shape)
    } else {
        Modifier
    }

    FixPixSurface(
        modifier = modifier
            .then(if (!isDark) Modifier.shadow(shadowElevation, shape, spotColor = Color.Black.copy(alpha = 0.1f)) else Modifier)
            .then(borderModifier),
        shape = shape,
        color = backgroundColor,
        content = content
    )
}
