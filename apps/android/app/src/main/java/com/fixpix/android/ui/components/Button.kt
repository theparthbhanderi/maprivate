package com.fixpix.android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.theme.FixPixTheme
import com.fixpix.android.ui.theme.FixPixMotion

/**
 * Button Variants matching FixPix website
 */
enum class ButtonVariant {
    Primary,    // Blue filled
    Secondary,  // Outlined
    Tinted,     // Light blue background
    Gray,       // Gray filled
    Destructive // Red filled
}

enum class ButtonSize {
    Small,
    Medium,
    Large
}

/**
 * FixPixButton - Production Grade iOS-style button
 * 
 * Features:
 * - Spring press animation (scale 0.97)
 * - Haptic feedback
 * - Multiple variants
 * - Loading state
 */
@Composable
fun FixPixButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Spring scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) FixPixMotion.SCALE_PRESSED else FixPixMotion.SCALE_NORMAL,
        animationSpec = FixPixMotion.springPress,
        label = "buttonScale"
    )
    
    // Colors based on variant
    val backgroundColor = when (variant) {
        ButtonVariant.Primary -> FixPixTheme.colors.primary
        ButtonVariant.Secondary -> Color.Transparent
        ButtonVariant.Tinted -> FixPixTheme.colors.primary.copy(alpha = 0.15f)
        ButtonVariant.Gray -> FixPixTheme.colors.fill
        ButtonVariant.Destructive -> FixPixTheme.colors.error
    }
    
    val contentColor = when (variant) {
        ButtonVariant.Primary -> FixPixTheme.colors.onPrimary
        ButtonVariant.Secondary -> FixPixTheme.colors.primary
        ButtonVariant.Tinted -> FixPixTheme.colors.primary
        ButtonVariant.Gray -> FixPixTheme.colors.textMain
        ButtonVariant.Destructive -> FixPixTheme.colors.onPrimary
    }
    
    val height = when (size) {
        ButtonSize.Small -> 32.dp
        ButtonSize.Medium -> 44.dp
        ButtonSize.Large -> 56.dp
    }
    
    val fontSize = when (size) {
        ButtonSize.Small -> 14.sp
        ButtonSize.Medium -> 17.sp
        ButtonSize.Large -> 18.sp
    }
    
    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .scale(scale)
            .height(height)
            .clip(FixPixTheme.shapes.large)
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = contentColor,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = text,
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f),
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
