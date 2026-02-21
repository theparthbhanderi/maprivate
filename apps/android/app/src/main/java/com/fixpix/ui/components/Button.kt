package com.fixpix.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixTheme

enum class ButtonVariant {
    Filled,
    Gray,
    Tinted,
    Plain,
    Destructive,
    Outline
}

enum class ButtonSize {
    Sm, Md, Lg
}

@Composable
fun FixPixButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Filled,
    size: ButtonSize = ButtonSize.Md,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val colors = FixPixTheme.colors
    val dimens = FixPixTheme.dimens
    val shapes = FixPixTheme.shapes
    
    // Resolve Colors based on variant
    val (backgroundColor, contentColor, border) = when (variant) {
        ButtonVariant.Filled -> Triple(colors.primary, Color.White, null)
        ButtonVariant.Gray -> Triple(colors.textPrimary.copy(alpha = 0.15f), colors.textPrimary, null)
        ButtonVariant.Tinted -> Triple(colors.primary.copy(alpha = 0.12f), colors.primary, null)
        ButtonVariant.Plain -> Triple(Color.Transparent, colors.primary, null)
        ButtonVariant.Destructive -> Triple(colors.error, Color.White, null)
        ButtonVariant.Outline -> Triple(Color.Transparent, colors.textPrimary, BorderStroke(1.dp, colors.separator.copy(0.3f)))
    }
    
    val disabledColor = backgroundColor.copy(alpha = 0.4f)
    val disabledContentColor = contentColor.copy(alpha = 0.4f)

    // Resolve Size
    val (height, padding, shape, textStyle) = when (size) {
        ButtonSize.Sm -> QueryButtonSize(dimens.buttonHeightSm, PaddingValues(horizontal = 14.dp), shapes.sm, FixPixTheme.typography.footnote.copy(fontWeight = FontWeight.SemiBold))
        ButtonSize.Md -> QueryButtonSize(dimens.buttonHeightMd, PaddingValues(horizontal = 20.dp), shapes.default, FixPixTheme.typography.subhead.copy(fontWeight = FontWeight.SemiBold))
        ButtonSize.Lg -> QueryButtonSize(dimens.buttonHeightLg, PaddingValues(horizontal = 24.dp), shapes.md, FixPixTheme.typography.headline.copy(fontWeight = FontWeight.SemiBold)) // 17px matches headline
    }

    // Spring Animation
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !loading) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 500f),
        label = "buttonScale"
    )

    FixPixSurface(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier),
        shape = shape,
        color = if (enabled) backgroundColor else disabledColor,
        contentColor = if (enabled) contentColor else disabledContentColor,
        border = border,
        elevation = 0.dp // Flat iOS flat style generally, add shadow only if needed specifically
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = height)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // No ripple for strictly iOS feel? Or subtle ripple? Let's use scale only for now as per audit.
                    enabled = enabled && !loading,
                    onClick = onClick
                )
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    if (leadingIcon != null) {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            leadingIcon()
                        }
                    }
                    
                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                         ProvideTextStyle(value = textStyle) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

// Helper to pass text style
@Composable
fun ProvideTextStyle(value: TextStyle, content: @Composable () -> Unit) {
    androidx.compose.material3.ProvideTextStyle(value, content)
}


data class QueryButtonSize(
    val height: Dp,
    val padding: PaddingValues,
    val shape: Shape,
    val textStyle: TextStyle
)
