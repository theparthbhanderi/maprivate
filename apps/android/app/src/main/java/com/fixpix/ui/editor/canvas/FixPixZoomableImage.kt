package com.fixpix.ui.editor.canvas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.fixpix.ui.theme.FixPixColors
import com.fixpix.ui.theme.FixPixShapes

/**
 * A collaborative image workspace that supports standard pan/zoom gestures.
 * Mirrors the 'react-zoom-pan-pinch' used on web.
 */
@Composable
fun FixPixZoomableImage(
    imagePainter: Painter,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FixPixColors.BackgroundDark // Always dark in editor
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    // Limits
    val minScale = 1f
    val maxScale = 5f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                    
                    // Simple pan logic - in prod needs bounds checking against viewport
                    // For typical "center bounds" behavior:
                    if (scale > 1f) {
                        val newOffset = offset + pan
                        // TODO: Add bound checks here based on scaled size
                        offset = newOffset
                    } else {
                        // Reset offset if zoomed out
                        offset = Offset.Zero 
                    }
                }
            }
    ) {
        // Image Container with graphicsLayer transformation
        Image(
            painter = imagePainter,
            contentDescription = "Workspace",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize() // Use full container size, let 'Fit' handle ratio
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        )
    }
}
