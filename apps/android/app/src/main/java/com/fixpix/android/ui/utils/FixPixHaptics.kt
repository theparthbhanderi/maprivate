package com.fixpix.android.ui.utils

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object FixPixHaptics {
    fun lightClick(haptic: HapticFeedback) {
        // Standard click feedback
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) 
    }

    fun mediumClick(haptic: HapticFeedback) {
         // Stronger interaction
         haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    fun success(haptic: HapticFeedback) {
        // Distinct success pattern if possible, else standard long press
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    
    fun error(haptic: HapticFeedback) {
        // Distinct error vibration
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        // In a real View system we'd usage HapticFeedbackConstants.REJECT
    }
    
    fun selectionChanged(haptic: HapticFeedback) {
         haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
}
