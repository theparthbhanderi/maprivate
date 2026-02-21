package com.fixpix.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import com.fixpix.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(500)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FixPixTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        // Logo (Text based for now as placeholder)
        Text(
            text = "FixPix",
            style = FixPixTheme.typography.largeTitle.copy(
                fontSize = FixPixTheme.typography.largeTitle.fontSize * 1.5f, // Even bigger
                fontWeight = FontWeight.Black
            ),
            color = FixPixTheme.colors.primary,
            modifier = Modifier.alpha(alphaAnim.value)
        )
    }
}
