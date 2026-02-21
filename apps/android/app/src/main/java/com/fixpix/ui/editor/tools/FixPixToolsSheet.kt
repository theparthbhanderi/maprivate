package com.fixpix.ui.editor.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixGlassSurface
import com.fixpix.ui.editor.state.FixPixEditorState
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun FixPixToolsSheet(
    state: FixPixEditorState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f)) // Base scrim
    ) {
        // 1. Tool Controls (If active tool selected)
        // Show Slider or Options
        AnimatedVisibility(
            visible = state.activeTool != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            state.activeTool?.let { tool ->
                ToolControlPanel(
                    tool = tool,
                    state = state
                )
            }
        }
        
        // 2. Tool List for Category
        val tools = remember(state.activeCategory) {
            when(state.activeCategory) {
                ToolCategory.ENHANCE -> EnhanceTools
                ToolCategory.MAGIC -> MagicTools
                ToolCategory.ADJUST -> AdjustmentTools
                ToolCategory.FILTERS -> FilterTools // Actually FilterTools is separate list
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tools.size) { index ->
                ToolIcon(
                    tool = tools[index],
                    isSelected = state.activeTool == tools[index],
                    onClick = { state.selectTool(tools[index]) }
                )
            }
        }

        // 3. Category Tabs
        val categories = ToolCategory.values()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E)) // Darker tab bar
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            categories.forEach { category ->
                CategoryTab(
                    category = category,
                    isSelected = state.activeCategory == category,
                    onClick = { state.activeCategory = category; state.activeTool = null }
                )
            }
        }
    }
}


@Composable
fun ToolControlPanel(
    tool: EditorTool,
    state: FixPixEditorState
) {
    FixPixGlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tool.label,
                style = FixPixTheme.typography.headline,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            when(tool) {
                is AdjustmentTool -> {
                    val value = state.getAdjustmentValue(tool)
                    Slider(
                        value = value,
                        onValueChange = { state.setAdjustmentValue(tool.id, it) },
                        valueRange = tool.min..tool.max,
                        colors = SliderDefaults.colors(
                            thumbColor = FixPixTheme.colors.primary,
                            activeTrackColor = FixPixTheme.colors.primary,
                            inactiveTrackColor = FixPixTheme.colors.fill
                        )
                    )
                }
                is UpscaleTool -> {
                     // Radio buttons logic, simplifying for now
                     Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                         listOf(Upscale1x, Upscale2x, Upscale4x).forEach { opt ->
                             FixPixChip(
                                 label = opt.label,
                                 selected = state.activeUpscale == opt,
                                 onClick = { state.setUpscaleOption(opt) }
                             )
                         }
                     }
                }
                // Toggle driven tools in Enhance/Magic
                FaceRestoreTool, ScratchRemovalTool, ColorizeTool, AutoEnhanceTool, RemoveBackgroundTool -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable", color = Color.White)
                        Switch(
                            checked = state.activeToggles.contains(tool.id),
                            onCheckedChange = { state.toggleOption(tool.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = FixPixTheme.colors.success
                            )
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ToolIcon(
    tool: EditorTool,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isSelected) FixPixTheme.colors.primary else FixPixTheme.colors.fill),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = tool.label,
                tint = if (isSelected) Color.White else Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = tool.label,
            style = FixPixTheme.typography.caption2,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@Composable
fun CategoryTab(
    category: ToolCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.label.uppercase(),
            style = FixPixTheme.typography.caption1,
            color = if (isSelected) FixPixTheme.colors.primary else Color.Gray
        )
    }
}

@Composable
fun FixPixChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(FixPixTheme.shapes.pill)
            .background(if (selected) FixPixTheme.colors.primary else FixPixTheme.colors.fill)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(label, color = if(selected) Color.White else Color.Gray, style = FixPixTheme.typography.caption1)
    }
}
