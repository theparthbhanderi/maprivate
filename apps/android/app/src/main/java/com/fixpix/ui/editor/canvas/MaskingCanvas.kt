package com.fixpix.ui.editor.canvas

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.fixpix.ui.editor.state.MaskPath

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaskingCanvas(
    paths: List<MaskPath>,
    currentPath: Path?,
    onPathStart: (Offset) -> Unit,
    onPathMove: (Offset) -> Unit,
    onPathEnd: () -> Unit,
    brushSize: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onPathStart(Offset(event.x, event.y))
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        onPathMove(Offset(event.x, event.y))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        onPathEnd()
                        true
                    }
                    else -> false
                }
            }
    ) {
        // Draw existing paths
        paths.forEach { maskPath ->
            drawPath(
                path = maskPath.path,
                color = Color.Red.copy(alpha = 0.5f),
                style = Stroke(
                    width = maskPath.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // Draw active path
        if (currentPath != null) {
            drawPath(
                path = currentPath,
                color = Color.Red.copy(alpha = 0.5f),
                style = Stroke(
                    width = brushSize,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
