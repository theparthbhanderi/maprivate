package com.fixpix.ui.editor.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.fixpix.ui.editor.state.FixPixEditorState

@Composable
fun MaskCanvas(
    state: FixPixEditorState, // We might use this to update bitmap later or get brush size
    brushSize: Float = 40f,
    modifier: Modifier = Modifier
) {
    // Local path state for immediate rendering
    // In a real app complexity, we'd use a more robust undo/redo system in the State
    val paths = remember { mutableStateListOf<Pair<Path, Float>>() }
    val currentPath = remember { Path() }
    
    val maskColor = Color(1f, 0f, 0f, 0.5f) // Red 50% opacity

    
    // Trigger redraw
    val drawTrigger = remember { mutableStateOf(0L) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath.moveTo(offset.x, offset.y)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        currentPath.lineTo(change.position.x, change.position.y)
                        drawTrigger.value = System.currentTimeMillis()
                    },
                    onDragEnd = {
                        paths.add(Pair(Path().apply { addPath(currentPath) }, brushSize))
                        currentPath.reset()
                        drawTrigger.value = System.currentTimeMillis()
                    }
                )
            }
    ) {
        // Read trigger to force redraw
        drawTrigger.value

        // Draw committed paths
        paths.forEach { (path, size) ->
            drawPath(
                path = path,
                color = maskColor,
                style = Stroke(
                    width = size,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
        
        // Draw active path
        drawPath(
            path = currentPath,
            color = maskColor,
            style = Stroke(
                width = brushSize,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
