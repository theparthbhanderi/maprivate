package com.fixpix.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixTheme

enum class ToggleSize {
    Default, Small
}

@Composable
fun FixPixToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    size: ToggleSize = ToggleSize.Default,
    enabled: Boolean = true
) {
    // Dimensions
    // Default: 51x31 track, 27px knob, 2px inset
    // Small: 44x26 track, 22px knob, 2px inset
    val trackWidth = if (size == ToggleSize.Default) 51.dp else 44.dp
    val trackHeight = if (size == ToggleSize.Default) 31.dp else 26.dp
    val knobSize = if (size == ToggleSize.Default) 27.dp else 22.dp
    val padding = 2.dp
    
    val trackColor by animateColorAsState(
        targetValue = if (checked) FixPixTheme.colors.success else Color(0x29787880), // Inactive gray
        label = "trackColor"
    )
    
    val knobOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - knobSize - padding else padding,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.6f),
        label = "knobOffset"
    )

    val toggleControl = @Composable {
        Box(
            modifier = Modifier
                .size(width = trackWidth, height = trackHeight)
                .background(
                    color = if (enabled) trackColor else trackColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(100) // Pill
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = enabled,
                    onClick = { onCheckedChange(!checked) }
                )
        ) {
            Box(
                modifier = Modifier
                    .offset(x = knobOffset)
                    .align(Alignment.CenterStart)
                    .size(knobSize)
                    .shadow(1.dp, CircleShape)
                    .background(Color.White, CircleShape)
            )
        }
    }

    if (label != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { onCheckedChange(!checked) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = FixPixTheme.typography.body,
                color = FixPixTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            toggleControl()
        }
    } else {
        Box(modifier = modifier) {
            toggleControl()
        }
    }
}
