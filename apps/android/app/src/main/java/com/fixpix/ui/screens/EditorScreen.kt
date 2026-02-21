package com.fixpix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.editor.canvas.FixPixImageWorkspace
import com.fixpix.ui.editor.overlays.FixPixProcessingOverlay
import com.fixpix.ui.editor.state.FixPixEditorState
import com.fixpix.ui.editor.tools.EditorActionBar
import com.fixpix.ui.editor.tools.FixPixToolsSheet

@Composable
fun EditorScreen(
    state: FixPixEditorState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // Main Container (Dark Theme for Editor)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Image Workspace (Z-Index: 0)
        // This handles panning, zooming, and displays the image/mask/comparison
        FixPixImageWorkspace(
            state = state,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top Navigation Bar (Z-Index: 1)
        // Simple overlay for Back button
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // 3. Bottom Controls (Z-Index: 1)
        // Includes the Tools Sheet and Action Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            // We stack them: Tools Sheet handles its own open/close visibility logic inside.
            // But structurally, it sits above the Action Bar roughly, or replaces it?
            // In the website, the tools panel is always visible or toggled?
            // In mobile, usually we have a bottom sheet.
            // Let's place the Tools Sheet. It has a scrim background.
            // We need to ensure it doesn't overlap the Action Bar if both are visible, 
            // or if the Action Bar is part of the sheet.
            // Website: "Sticky Bottom Action Bar" is separate.
            
            // Layout:
            // [ Tools Panel (Variable Height) ]
            // [ Action Bar (Fixed Height) ]
            
            // However, FixPixToolsSheet implementation includes a scrim and category tabs.
            // Let's lay them out vertically.
            
            androidx.compose.foundation.layout.Column(
                 modifier = Modifier.fillMaxWidth()
            ) {
                // Tools Panel
                FixPixToolsSheet(
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Sticky Action Bar
                EditorActionBar(
                    onGenerateClick = {
                        // Demo Processing Flow
                        state.setProcessing(true, "Enhancing...")
                        // In real app, launch VM job
                    },
                    onSaveClick = onSaveClick,
                    onCancelClick = onBackClick,
                    isProcessing = state.isProcessing
                )
            }
        }

        // 4. Processing Overlay (Z-Index: 2)
        // Fullscreen block when processing
        FixPixProcessingOverlay(
            isVisible = state.isProcessing,
            currentStep = state.processingStep
        )
    }
}
