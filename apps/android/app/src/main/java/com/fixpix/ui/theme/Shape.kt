package com.fixpix.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class FixPixShapes(
    val sm: Shape,
    val default: Shape,
    val md: Shape,
    val lg: Shape,
    val xl: Shape,
    val xxl: Shape,
    val xxxl: Shape,
    val pill: Shape
)

val DefaultShapes = FixPixShapes(
    sm = RoundedCornerShape(8.dp),
    default = RoundedCornerShape(10.dp),
    md = RoundedCornerShape(12.dp),
    lg = RoundedCornerShape(14.dp),
    xl = RoundedCornerShape(16.dp), // Card Standard
    xxl = RoundedCornerShape(20.dp),
    xxxl = RoundedCornerShape(24.dp),
    pill = RoundedCornerShape(Percent(50))
)

// Helper for Pill Shape percentage, creates a circular shape based on height
private fun Percent(percent: Int) = percent // Just a placeholder logic, RoundedCornerShape(50) is actually pixel. 
// Correct way for pill in Compose is RoundedCornerShape(50) if we want percentage? No, RoundedCornerShape(50.dp) is 50dp. 
// RoundedCornerShape(50) is 50%.
// Let's use RoundedCornerShape(100) for full pill which is basically circle for square or pill for rect.
// Wait, RoundedCornerShape(percent: Int) constructor exists.

val PillShape = RoundedCornerShape(50) 

val FixPixDefaultShapes = FixPixShapes(
    sm = RoundedCornerShape(8.dp),
    default = RoundedCornerShape(10.dp),
    md = RoundedCornerShape(12.dp),
    lg = RoundedCornerShape(14.dp),
    xl = RoundedCornerShape(16.dp),
    xxl = RoundedCornerShape(20.dp),
    xxxl = RoundedCornerShape(24.dp),
    pill = RoundedCornerShape(50) // 50 percent
)


val LocalFixPixShapes = staticCompositionLocalOf { FixPixDefaultShapes }
