package com.fixpix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun FixPixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    error: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val colors = FixPixTheme.colors
    val shapes = FixPixTheme.shapes
    
    val borderColor = when {
        error -> colors.error
        isFocused -> colors.primary.copy(alpha = 0.5f)
        else -> Color.Transparent
    }
    
    val backgroundColor = colors.textPrimary.copy(alpha = 0.08f) // Fill/0.08 approx

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor, shapes.default)
            .border(
                width = if (isFocused || error) 1.dp else 0.dp,
                color = borderColor,
                shape = shapes.default
            ),
        enabled = true,
        readOnly = false,
        textStyle = FixPixTheme.typography.body.copy(color = colors.textPrimary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(colors.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Leading Icon
                if (leadingIcon != null) {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        leadingIcon()
                    }
                }
                
                // Placeholder
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = FixPixTheme.typography.body,
                        color = colors.textTertiary,
                        modifier = Modifier.padding(start = if (leadingIcon != null) 32.dp else 0.dp)
                    )
                }
                
                // Content
                Box(
                    modifier = Modifier.padding(
                        start = if (leadingIcon != null) 32.dp else 0.dp,
                        end = if (trailingIcon != null) 32.dp else 0.dp
                    )
                ) {
                    innerTextField()
                }
                
                // Trailing Icon
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        trailingIcon()
                    }
                }
            }
        }
    )
}
