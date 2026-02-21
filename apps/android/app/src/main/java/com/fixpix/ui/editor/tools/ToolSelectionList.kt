package com.fixpix.ui.editor.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.editor.state.EditorTool
import com.fixpix.ui.theme.FixPixColors
import com.fixpix.ui.theme.FixPixTypography

@Composable
fun ToolSelectionList(
    tools: List<EditorTool>,
    activeTool: EditorTool?,
    onToolSelected: (EditorTool) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(tools) { tool ->
            ToolItem(
                tool = tool,
                isSelected = tool == activeTool,
                onClick = { onToolSelected(tool) }
            )
        }
    }
}

@Composable
fun ToolItem(
    tool: EditorTool,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor = if (isSelected) FixPixColors.Blue else FixPixColors.SystemGray
    val textColor = if (isSelected) FixPixColors.Blue else FixPixColors.SystemGray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isSelected) FixPixColors.Blue.copy(alpha = 0.1f) else Color.Transparent)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            if (tool.icon != null) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = tool.label,
                    tint = iconColor
                )
            } else {
                // Fallback Logic or Specific Icons map
                // For now, let's use first letter if no icon, or Generic
                // In real app, all tools have icons.
                Text(
                    text = tool.label.take(1),
                    style = FixPixTypography.Headline,
                    color = iconColor
                )
            }
        }
        Text(
            text = tool.label,
            style = FixPixTypography.Caption1,
            color = textColor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
