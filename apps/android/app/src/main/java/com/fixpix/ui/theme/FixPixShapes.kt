package com.fixpix.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object FixPixShapes {
    val small = RoundedCornerShape(8.dp)       // ios-sm
    val medium = RoundedCornerShape(10.dp)      // ios (inputs)
    val large = RoundedCornerShape(16.dp)       // ios-xl (cards)
    val extraLarge = RoundedCornerShape(24.dp)  // ios-3xl (sheets)
    val pill = RoundedCornerShape(50)           // buttons
}
