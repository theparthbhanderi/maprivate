package com.fixpix.android.ui.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fixpix.android.ui.components.editor.EditorSettings
import com.fixpix.android.ui.components.editor.EmptyStateUpload
import com.fixpix.android.ui.components.editor.ImageCanvas
import com.fixpix.android.ui.components.editor.ProcessingOverlay
import com.fixpix.android.ui.components.editor.ToolsBottomSheet
import com.fixpix.android.ui.components.editor.ToolsFAB
import com.fixpix.android.ui.components.editor.ZoomControls

/**
 * EditorScreen - Complete editor matching website audit
 * 
 * Layout:
 * - Dark canvas background (0xFF0A0A0C)
 * - Image workspace with zoom/pan
 * - Floating zoom controls (bottom-center)
 * - Tools FAB (bottom-right)
 * - Tools bottom sheet with 4 tabs
 * - Processing overlay
 */
@Composable
fun EditorScreen(
    projectId: String,
    navController: NavController
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showTools by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var processingStatus by remember { mutableStateOf("Processing...") }
    var settings by remember { mutableStateOf(EditorSettings()) }
    var isMaskMode by remember { mutableStateOf(false) }
    
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0C))
    ) {
        // Layer 1: Canvas or Empty State
        if (imageUri != null) {
            ImageCanvas(
                imageUri = imageUri.toString(),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            EmptyStateUpload(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Layer 2: Zoom Controls (only when image loaded)
        if (imageUri != null) {
            ZoomControls(
                onZoomIn = { /* TODO: Increase zoom */ },
                onZoomOut = { /* TODO: Decrease zoom */ },
                onFit = { /* TODO: Reset zoom */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            )
        }
        
        // Layer 3: Tools FAB (bottom-right)
        ToolsFAB(
            onClick = { showTools = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
        
        // Layer 4: Tools Bottom Sheet
        ToolsBottomSheet(
            visible = showTools,
            onDismiss = { showTools = false },
            settings = settings,
            onSettingsChange = { settings = it },
            onObjectEraserClick = {
                showTools = false
                isMaskMode = true
            },
            onGenerate = {
                showTools = false
                isProcessing = true
                processingStatus = "Enhancing image..."
                // TODO: Call backend API
            }
        )
        
        // Layer 5: Processing Overlay
        ProcessingOverlay(
            visible = isProcessing,
            statusText = processingStatus,
            modifier = Modifier.fillMaxSize()
        )
    }
}
