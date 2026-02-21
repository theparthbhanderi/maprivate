package com.fixpix.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun FixPixDivider(
    modifier: Modifier = Modifier,
    color: Color = FixPixTheme.colors.separator,
    thickness: Dp = 1.dp // Standard separator line
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}
