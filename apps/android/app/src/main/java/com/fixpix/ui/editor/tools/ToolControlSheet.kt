package com.fixpix.ui.editor.tools

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixGlassCard
import com.fixpix.ui.components.FixPixSlider
import com.fixpix.ui.components.FixPixToggle
import com.fixpix.ui.editor.state.EditorTool
import com.fixpix.ui.theme.FixPixColors
import com.fixpix.ui.theme.FixPixTypography

/**
 * The floating bottom sheet that contains specific controls for the active tool.
 */
@Composable
fun ToolControlSheet(
    activeTool: EditorTool?,
    adjustmentValue: Float,
    onAdjustmentChange: (Float) -> Unit,
    toggleState: Boolean,
    onToggleChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeTool == null) return

    FixPixGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // Check: ensure not conflicting with bottom nav
        shape = RoundedCornerShape(24.dp),
        backgroundColor = FixPixColors.SurfaceLight.copy(alpha = 0.9f) // High opacity
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag Handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(FixPixColors.SystemGray4)
                    .padding(bottom = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Tool Title
            Text(
                text = activeTool.label,
                style = FixPixTypography.Headline,
                color = FixPixColors.LabelPrimaryLight
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            // Description
            if (activeTool.description.isNotEmpty()) {
                Text(
                    text = activeTool.description,
                    style = FixPixTypography.Caption1,
                    color = FixPixColors.SystemGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Controls based on Type
            when (activeTool) {
                is EditorTool.Adjustment -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${(adjustmentValue * 100).toInt()}%",
                            style = FixPixTypography.Caption1,
                            modifier = Modifier.width(40.dp)
                        )
                        FixPixSlider(
                            value = adjustmentValue,
                            onValueChange = onAdjustmentChange,
                            valueRange = activeTool.min..activeTool.max,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                EditorTool.Restore, 
                EditorTool.FaceEnhance, 
                EditorTool.Colorize, 
                EditorTool.Upscale,
                EditorTool.RemoveBackground -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable", style = FixPixTypography.Body)
                        FixPixToggle(
                            checked = toggleState,
                            onCheckedChange = onToggleChange
                        )
                    }
                }
                
                else -> {
                    Text("Select options above", style = FixPixTypography.Caption1)
                }
            }
        }
    }
}
