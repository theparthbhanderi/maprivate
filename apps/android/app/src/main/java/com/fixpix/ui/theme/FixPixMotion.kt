package com.fixpix.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

/**
 * Standard iOS-like motion curves and durations.
 */
object FixPixMotion {
    // iOS Spring-like easing
    val EasingOvershoot = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f) // Custom bounce
    val EasingStandard = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f) // EaseOutQuad-ish
    
    // Durations
    const val DurationShort = 200
    const val DurationMedium = 300
    const val DurationLong = 500

    // Transitions
    // Usage: EnterTransition = FixPixMotion.ScreenEnter
    val ScreenEnter = slideInHorizontally(
        initialOffsetX = { it }, 
        animationSpec = tween(DurationMedium, easing = EasingStandard)
    ) + fadeIn(tween(DurationMedium))
    
    val ScreenExit = slideOutHorizontally(
        targetOffsetX = { -it / 3 }, // iOS parallax-ish
        animationSpec = tween(DurationMedium, easing = EasingStandard)
    ) + fadeOut(tween(DurationMedium))
    
    val ScreenPopEnter = slideInHorizontally(
        initialOffsetX = { -it / 3 },
        animationSpec = tween(DurationMedium, easing = EasingStandard)
    ) + fadeIn(tween(DurationMedium))
    
    val ScreenPopExit = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(DurationMedium, easing = EasingStandard)
    ) + fadeOut(tween(DurationMedium))
}
