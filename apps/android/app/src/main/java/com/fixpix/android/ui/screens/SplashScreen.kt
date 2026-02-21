package com.fixpix.android.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

/**
 * SplashScreen - Animated logo reveal and navigation
 */
@Composable
fun SplashScreen(
    navController: NavController
) {
    var showLogo by remember { mutableStateOf(false) }
    
    val logoAlpha by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0f,
        animationSpec = tween(500),
        label = "logoAlpha"
    )
    
    LaunchedEffect(Unit) {
        delay(200)
        showLogo = true
        delay(1500)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FixPixTheme.colors.background)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        FixPixTheme.colors.primary.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset.Zero,
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(logoAlpha)
        ) {
            Text(
                text = "FixPix",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = FixPixTheme.colors.primary,
                letterSpacing = (-2).sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "AI Photo Restoration",
                fontSize = 16.sp,
                color = FixPixTheme.colors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
