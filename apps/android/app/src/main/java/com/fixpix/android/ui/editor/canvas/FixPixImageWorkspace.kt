package com.fixpix.android.ui.editor.canvas

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.fixpix.android.ui.editor.state.EditorMode
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun FixPixImageWorkspace(
    originalUri: Uri?,
    processedUri: Uri?,
    mode: EditorMode,
    maskPaths: List<androidx.compose.ui.graphics.Path> = emptyList(),
    onMaskPathAdd: (androidx.compose.ui.graphics.Path) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Zoom/Pan State
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)) // Dark workspace bg
            .pointerInput(mode) {
                // Disable Pan/Zoom when Masking to allow drawing
                if (originalUri != null && mode != EditorMode.MASKING) {
                    detectTransformGestures { _, pan, zoom, _ ->
                         // Physics: Add friction at bounds or limits?
                         // For now, strict limits 0.5x to 5x
                        scale = (scale * zoom).coerceIn(0.5f, 5f)
                        offset += (pan * zoom) // Pan moves faster when zoomed in? No, standard pan.
                    }
                }
            }
    ) {
        if (originalUri == null) {
            EmptyState()
        } else {
            // Transformable Content Layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Efficient Image Loader with Coil
                val context = LocalContext.current
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(originalUri)
                        .crossfade(true)
                        .build()
                )
                
                // Loading / Error Handling
                if (painter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(color = FixPixTheme.colors.primary)
                }
                
                Image(
                    painter = painter,
                    contentDescription = "Original",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                // Grid Overlay
                if (scale > 1.2f || mode == EditorMode.CROPPING) {
                    GridOverlay()
                }

                // Modes
                if (mode == EditorMode.MASKING) {
                     // Mask Overlay
                     Box(modifier = Modifier.fillMaxSize()) {
                         MaskCanvas(
                             paths = maskPaths,
                             onPathAdd = onMaskPathAdd,
                             brushSize = 40f, // TODO: Pass from settings
                             modifier = Modifier.fillMaxSize()
                         )
                     }
                }
                
                if (mode == EditorMode.PROCESSED && processedUri != null) {
                    val processedPainter = rememberAsyncImagePainter(processedUri)
                    Image(
                        painter = processedPainter,
                        contentDescription = "Processed",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Select or Upload an Image",
            style = FixPixTheme.typography.headline,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun GridOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val color = Color.White.copy(alpha = 0.1f)
        val stroke = 1.dp.toPx()
        
        // 3x3 Grid
        drawLine(color, Offset(width/3, 0f), Offset(width/3, height), stroke)
        drawLine(color, Offset(2*width/3, 0f), Offset(2*width/3, height), stroke)
        drawLine(color, Offset(0f, height/3), Offset(width, height/3), stroke)
        drawLine(color, Offset(0f, 2*height/3), Offset(width, 2*height/3), stroke)
    }
}
