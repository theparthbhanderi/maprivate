package com.fixpix.android.ui.motion

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset

object FixPixMotion {
    // Physics-based Spring for Touch Feedback (iOS-like)
    val SpringPress = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Standard Transitions
    // Slide Up and Fade In (y 20 -> 0)
    val EntryTransition = fadeIn(animationSpec = tween(300)) + 
                          slideInVertically(
                              initialOffsetY = { 40 }, // Approx 20dp in pixels roughly
                              animationSpec = spring(
                                  dampingRatio = 0.8f, 
                                  stiffness = 300f
                              )
                          )
    
    val ExitTransition = fadeOut(animationSpec = tween(200))

    // Staggered List Item Delay
    fun getStaggerDelay(index: Int, baseDelay: Int = 50): Int {
        return index * baseDelay
    }
}
