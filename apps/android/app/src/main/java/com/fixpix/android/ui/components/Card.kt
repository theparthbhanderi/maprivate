package com.fixpix.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * FixPixCard - iOS-style card container
 * 
 * Features:
 * - Soft shadow matching website
 * - Rounded corners (16dp default)
 * - Surface color from theme
 */
@Composable
fun FixPixCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation,
                shape = FixPixTheme.shapes.large,
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(FixPixTheme.shapes.large)
            .background(FixPixTheme.colors.surface)
            .padding(FixPixTheme.spacing.lg)
    ) {
        content()
    }
}

/**
 * FixPixCard with click support
 */
@Composable
fun FixPixCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation,
                shape = FixPixTheme.shapes.large,
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(FixPixTheme.shapes.large)
            .background(FixPixTheme.colors.surface)
            .clickable(onClick = onClick)
            .padding(FixPixTheme.spacing.lg)
    ) {
        content()
    }
}
