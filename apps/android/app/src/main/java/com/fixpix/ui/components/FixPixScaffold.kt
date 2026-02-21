package com.fixpix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun FixPixScaffold(
    modifier: Modifier = Modifier,
    topBarTitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    background: Color = FixPixTheme.colors.background,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = background,
        topBar = {
            if (topBarTitle != null) {
                FixPixGlassTopBar(
                    title = topBarTitle,
                    onBackClick = onBackClick
                )
            }
        },
        content = content
    )
}

@Composable
fun FixPixGlassTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(FixPixTheme.colors.surface.copy(alpha = 0.8f)) // Glass opacity
            // Add real blur modifier here via library in production
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(44.dp) // iOS Standard Nav Bar height
    ) {
        // Back Button
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = FixPixTheme.colors.primary
                )
            }
        }

        // Search/Center Title
        Text(
            text = title,
            style = FixPixTheme.typography.headline, // Bold 17sp
            color = FixPixTheme.colors.textPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Add bottom separator line
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(0.5.dp) // Hairline
                .background(FixPixTheme.colors.separator)
        )
    }
}
