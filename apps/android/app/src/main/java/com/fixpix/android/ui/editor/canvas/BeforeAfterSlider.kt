package com.fixpix.android.ui.editor.canvas

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fixpix.android.ui.components.FixPixSurface
import com.fixpix.android.ui.theme.FixPixTheme
import kotlin.math.roundToInt

@Composable
fun BeforeAfterSlider(
    original: Any?, // Uri or painter source
    processed: Any?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val maxWidth = constraints.maxWidth.toFloat()
        var dragOffset by remember { mutableFloatStateOf(maxWidth / 2f) }

        // 1. Background: Original Image (Full)
        Image(
            painter = rememberAsyncImagePainter(original),
            contentDescription = "Before",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Foreground: Processed Image (Clipped)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    // Custom Clip Shape based on dragOffset would be ideal, 
                    // but for simple column clip we can use width modifier on a container aligned start
                    androidx.compose.ui.graphics.RectangleShape 
                )
        ) {
             // We need to crop the image container, not just shrink it, to maintain alignment.
             // In Compose, clip(Rect) is efficient. 
             // Simplified approach: Render processed image in a box width = dragOffset
             
             Box(
                 modifier = Modifier
                     .width(with(LocalDensity.current) { dragOffset.toDp() })
                     .fillMaxHeight()
                     .clip(androidx.compose.ui.graphics.RectangleShape)
             ) {
                 Image(
                    painter = rememberAsyncImagePainter(processed),
                    contentDescription = "After",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(with(LocalDensity.current) { maxWidth.toDp() }) // Force full width inside clipped box
                 )
             }
        }

        // 3. Slider Handle
        Box(
            modifier = Modifier
                .offset { IntOffset(dragOffset.roundToInt(), 0) }
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        dragOffset = (dragOffset + delta).coerceIn(0f, maxWidth)
                    }
                )
        ) {
            // Handle Circle
            FixPixSurface(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
                    .offset(x = (-15).dp), // Center the 32dp circle on the 2dp line
                shape = CircleShape,
                color = Color.White,
                elevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Code, // Placeholder for arrows <->
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = FixPixTheme.colors.primary
                    )
                }
            }
        }
    }
}
