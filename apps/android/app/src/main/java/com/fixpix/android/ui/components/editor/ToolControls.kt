package com.fixpix.android.ui.components.editor

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * Tool Toggle Row - iOS-style toggle for tool options
 */
@Composable
fun ToolToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            color = if (enabled) FixPixTheme.colors.textMain else FixPixTheme.colors.textSecondary
        )
        
        // iOS-style toggle
        val backgroundColor by animateColorAsState(
            targetValue = if (checked) FixPixTheme.colors.success else FixPixTheme.colors.fill.copy(alpha = 0.16f),
            label = "toggleBg"
        )
        val thumbOffset by animateFloatAsState(
            targetValue = if (checked) 20f else 0f,
            label = "thumbOffset"
        )
        
        Box(
            modifier = Modifier
                .width(51.dp)
                .height(31.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = enabled,
                    onClick = { onCheckedChange(!checked) }
                )
                .padding(2.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .padding(start = thumbOffset.dp)
                    .width(27.dp)
                    .height(27.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
            )
        }
    }
}

/**
 * Segmented Control for Upscaling (Off/2x/4x)
 */
@Composable
fun UpscaleSegmentedControl(
    selected: Int, // 0=Off, 1=2x, 2=4x
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("Off", "2x", "4x")
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Upscaling",
            fontSize = 17.sp,
            color = FixPixTheme.colors.textMain
        )
        
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(FixPixTheme.colors.fill.copy(alpha = 0.12f))
        ) {
            options.forEachIndexed { index, label ->
                val isSelected = selected == index
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) FixPixTheme.colors.primary else Color.Transparent,
                    label = "segmentBg"
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgColor)
                        .clickable { onSelectionChange(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Color.White else FixPixTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}

/**
 * Tool Slider Row for Adjust panel
 */
@Composable
fun ToolSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..2f
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 15.sp,
                color = FixPixTheme.colors.textMain
            )
            Text(
                text = String.format("%.1f", value),
                fontSize = 15.sp,
                color = FixPixTheme.colors.textSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        androidx.compose.material3.Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = FixPixTheme.colors.primary,
                activeTrackColor = FixPixTheme.colors.primary,
                inactiveTrackColor = FixPixTheme.colors.fill.copy(alpha = 0.3f)
            )
        )
    }
}

/**
 * Filter Grid Item
 */
@Composable
fun FilterGridItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) FixPixTheme.colors.primary else FixPixTheme.colors.fill.copy(alpha = 0.12f),
        label = "filterBg"
    )
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else FixPixTheme.colors.textMain
        )
    }
}
