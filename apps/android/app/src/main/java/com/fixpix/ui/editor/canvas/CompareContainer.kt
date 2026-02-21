package com.fixpix.ui.editor.canvas

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixGlassCard
import com.fixpix.ui.theme.FixPixTypography

/**
 * Handles "Press and Hold" comparison logic.
 */
@Composable
fun CompareContainer(
    originalPainter: Painter,
    processedPainter: Painter,
    modifier: Modifier = Modifier,
    content: @Composable (Painter) -> Unit
) {
    var isHolding by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isHolding = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        tryAwaitRelease()
                        isHolding = false
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) // Light thump
                    }
                )
            }
    ) {
        // Render content dependent on holding state
        content(if (isHolding) originalPainter else processedPainter)

        // "Original" Badge
        if (isHolding) {
            FixPixGlassCard(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp),
                backgroundColor = Color.Black.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "Original",
                    style = FixPixTypography.Caption1,
                    color = Color.White
                )
            }
        }
    }
}
