package com.fixpix.ui.editor.overlays

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixColors
import com.fixpix.ui.theme.FixPixTypography

@Composable
fun ScanningOverlay(
    isProcessing: Boolean,
    statusText: String
) {
    if (!isProcessing) return

    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        // Scanning Line Animation (Vertical Scan)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        offsetY to FixPixColors.Blue.copy(alpha = 0.5f),
                        offsetY + 0.05f to FixPixColors.Blue,
                        offsetY + 0.1f to Color.Transparent,
                        startY = 0f,
                        endY = 2000f // Arbitrary height approx
                    )
                )
        )

        // Status Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AI Processing",
                style = FixPixTypography.Title2,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = statusText,
                style = FixPixTypography.Body,
                color = FixPixColors.Blue // Pulse this ideally
            )
        }
    }
}
