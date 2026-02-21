package com.fixpix.ui.editor.canvas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BeforeAfterSlider(
    original: ImageBitmap?,
    processed: ImageBitmap?,
    position: Float, // 0.0 to 1.0 (Percentage of Original visible from left)
    onPositionChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    if (original == null || processed == null) return

    val haptic = LocalHapticFeedback.current

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        
        // 1. Background Layer (Processed / After) - Full width
        // We assume 'After' is the goal, so it's the base.
        // Usually, Left is Before, Right is After.
        // So Background = After.
        Image(
            bitmap = processed,
            contentDescription = "After",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit // Ensure match
        )

        // 2. Foreground Layer (Original / Before) - Clipped
        // Clip width = position * totalWidth
        // We need a custom clip shape or simple width modifier if alignment implies clipping
        // Box with fixed width alignment start works? No, scaling might break.
        // Best is graphicsLayer clip.
        // But simpler: Box(Modifier.width(px)) { Image(.., contentScale = Fit, alignment = CenterStart) } 
        // Image must maintain aspect ratio relative to full size.
        // So: Box(Modifier.clip(Rect(0,0, splitX, H)))
        
        val splitX = width * position
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(androidx.compose.ui.graphics.GenericShape { size, _ ->
                     moveTo(0f, 0f)
                     lineTo(splitX, 0f)
                     lineTo(splitX, size.height)
                     lineTo(0f, size.height)
                     close()
                })
        ) {
             Image(
                bitmap = original,
                contentDescription = "Before",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        }

        // 3. Slider Handle
        Box(
            modifier = Modifier
                .offset { IntOffset(splitX.roundToInt(), 0) }
                .fillMaxHeight()
                .width(44.dp) // Touch target width
                .offset(x = (-22).dp) // Center horizontally on splitX
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newPos = (splitX + delta).coerceIn(0f, width)
                        onPositionChange(newPos / width)
                        // Weak haptic on bounds?
                    },
                    onDragStopped = {
                        // Spring physics? Prompt says "Spring physics on release".
                        // Logic would go here to animate back to center or nearest notch via state
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                )
        ) {
            // Visual Line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.White)
                    .align(Alignment.Center)
            )
            
            // Visual Handle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Code, // Arrows < >
                    contentDescription = "Slider",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
