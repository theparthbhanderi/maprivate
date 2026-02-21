package com.fixpix.android.ui.editor.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun MaskCanvas(
    paths: List<Path>,
    onPathAdd: (Path) -> Unit,
    brushSize: Float,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    // Current drawing state
    var currentPath by remember { mutableStateOf<Path?>(null) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isEnabled) {
                if (!isEnabled) return@pointerInput
                
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath = Path().apply { moveTo(offset.x, offset.y) }
                    },
                    onDrag = { _, dragAmount ->
                        currentPath?.let { path ->
                            // We need to track last point to use quadraticBezierTo for smoothing
                            // but for simple MVP, relativeLineTo works
                            path.relativeLineTo(dragAmount.x, dragAmount.y)
                        }
                    },
                    onDragEnd = {
                        currentPath?.let {
                             onPathAdd(it)
                             currentPath = null
                        }
                    }
                )
            }
    ) {
        // Draw existing paths
        paths.forEach { path ->
            drawPath(
                path = path,
                color = Color(0xFFFF3B30).copy(alpha = 0.5f), // Red mask
                style = Stroke(
                    width = brushSize,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
        
        // Draw current path
        currentPath?.let { path ->
            drawPath(
                path = path,
                color = Color(0xFFFF3B30).copy(alpha = 0.5f),
                style = Stroke(
                    width = brushSize,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
