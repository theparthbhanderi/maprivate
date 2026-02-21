package com.fixpix.android.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * FixPix Motion System - Production Grade
 * 
 * iOS-inspired animation curves and durations
 */

@Immutable
object FixPixMotion {
    
    // =====================================================
    // DURATIONS (milliseconds)
    // =====================================================
    
    const val INSTANT = 0
    const val FAST = 100
    const val NORMAL = 200
    const val SLOW = 300
    const val SHEET = 350
    const val PAGE = 400
    
    // =====================================================
    // SPRING PHYSICS
    // =====================================================
    
    /** Light spring for subtle effects (toggles, small movements) */
    val springLight: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )
    
    /** Standard spring for most interactions */
    val springStandard: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    /** Heavy spring for large movements (sheets, pages) */
    val springHeavy: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    /** Press effect spring (buttons, cards) */
    val springPress: AnimationSpec<Float> = spring(
        dampingRatio = 0.5f,
        stiffness = 500f
    )
    
    // =====================================================
    // TWEEN ANIMATIONS
    // =====================================================
    
    val tweenFast = tween<Float>(durationMillis = FAST)
    val tweenNormal = tween<Float>(durationMillis = NORMAL)
    val tweenSlow = tween<Float>(durationMillis = SLOW)
    
    // =====================================================
    // ENTER/EXIT ANIMATIONS
    // =====================================================
    
    val fadeInStandard = fadeIn(animationSpec = tween(NORMAL))
    val fadeOutStandard = fadeOut(animationSpec = tween(NORMAL))
    
    val slideInFromBottom = slideInVertically(
        animationSpec = tween(SHEET),
        initialOffsetY = { it }
    )
    val slideOutToBottom = slideOutVertically(
        animationSpec = tween(SHEET),
        targetOffsetY = { it }
    )
    
    // =====================================================
    // SCALE VALUES
    // =====================================================
    
    const val SCALE_PRESSED = 0.97f
    const val SCALE_NORMAL = 1.0f
    const val SCALE_EMPHASIZED = 1.05f
}

val LocalFixPixMotion = staticCompositionLocalOf { FixPixMotion }
