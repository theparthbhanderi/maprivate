package com.fixpix.android.ui.components.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fixpix.android.ui.theme.FixPixTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

/**
 * Zoomable/pannable image canvas with grid background
 */
@Composable
fun ImageCanvas(
    imageUri: String?,
    modifier: Modifier = Modifier,
    onImageTap: () -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0C)) // Dark canvas background
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 4f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Subtle grid background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 40.dp.toPx()
            val dotColor = Color.White.copy(alpha = 0.05f)
            val dotRadius = 1.dp.toPx()
            
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(dotColor, dotRadius, Offset(x, y))
                    y += gridSpacing
                }
                x += gridSpacing
            }
        }
        
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Editor Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
            )
        }
    }
}

/**
 * Zoom controls floating pill
 */
@Composable
fun ZoomControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onFit: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .background(
                FixPixTheme.colors.surface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ZoomButton("-", onClick = onZoomOut)
        ZoomButton("Fit", onClick = onFit)
        ZoomButton("+", onClick = onZoomIn)
    }
}

@Composable
private fun ZoomButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                FixPixTheme.colors.fill.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = FixPixTheme.colors.textMain,
            fontSize = 14.sp
        )
    }
}
