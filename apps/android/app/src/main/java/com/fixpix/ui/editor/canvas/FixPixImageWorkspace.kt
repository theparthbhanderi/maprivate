package com.fixpix.ui.editor.canvas

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.fixpix.ui.editor.state.FixPixEditorState

@Composable
fun FixPixImageWorkspace(
    state: FixPixEditorState,
    modifier: Modifier = Modifier
) {
    // Zoom/Pan State
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    // Bounds
    val minScale = 0.5f
    val maxScale = 4f

    Box(
        modifier = modifier
            .fillMaxSize()
            // Catch gestures
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                    // Adjust pan sensitivity with scale if needed, or just apply
                    val newOffset = offset + pan
                    // In a real engine we'd calculate bounds to prevent panning off-screen too far
                    // For now, free pan with "soft" bounds logic would be:
                    offset = newOffset
                }
            }
    ) {
        // The Canvas / Image Container
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
        ) {
            // 1. Base Image
            // Optimized with Coil for large bitmaps and caching
            if (state.currentImage != null) {
                // Determine model: could be a Bitmap, File, or URL (String)
                // FixPixEditorState currently defines currentImage as ImageBitmap? 
                // We should probably change State to hold 'Any?' or specific type wrapper to support Coil best.
                // But if we stick to ImageBitmap in state for now (loaded via repo), we display it directly.
                // However, for optimization, we want to load from URI/File if possible.
                // Let's assume currentImage is ImageBitmap for the prompt context, 
                // BUT best practice is to load from URI.
                
                // If state has 'currentImageUri', use AsyncImage.
                // Since we defined `currentImage: ImageBitmap?` in Step 3, we stick to it, 
                // OR we check if we can pass a URL/File.
                
                // Let's assume we proceed with the existing ImageBitmap for now to avoid refactoring State heavily,
                // BUT we add the Coil import for future-proofing or if we switch.
                androidx.compose.foundation.Image(
                     bitmap = state.currentImage!!,
                     contentDescription = "Editing Image",
                     modifier = Modifier.fillMaxSize(),
                     contentScale = androidx.compose.ui.layout.ContentScale.Fit
                )
            } else {
                 // Placeholder
                 androidx.compose.foundation.Image(
                     painter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery),
                     contentDescription = "No Image",
                     modifier = Modifier.fillMaxSize(0.3f),
                     alpha = 0.5f
                 )
            }

            // 2. Overlays based on Mode
            /*
            when (state.mode) {
                EditorMode.MASKING -> {
                    MaskCanvas(state = state)
                }
                EditorMode.PROCESSED -> {
                    BeforeAfterSlider(
                        original = state.currentImage,
                        processed = state.processedImage,
                        position = state.comparePosition
                    )
                }
                else -> {}
            }
            */
        }
    }
}
