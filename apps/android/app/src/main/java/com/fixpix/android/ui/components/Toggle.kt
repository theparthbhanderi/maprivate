package com.fixpix.android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * FixPixToggle - iOS-style switch component
 * 
 * Matches UISwitch from iOS with animated thumb and track
 */
@Composable
fun FixPixToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val hapticFeedback = LocalHapticFeedback.current
    
    // Animated colors
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> FixPixTheme.colors.fill.copy(alpha = 0.12f)
            checked -> FixPixTheme.colors.success
            else -> FixPixTheme.colors.fill.copy(alpha = 0.16f)
        },
        label = "trackColor"
    )
    
    // Animated thumb position
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        label = "thumbOffset"
    )
    
    Box(
        modifier = modifier
            .width(51.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCheckedChange(!checked)
                }
            )
            .padding(2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .width(27.dp)
                .height(27.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
        )
    }
}
