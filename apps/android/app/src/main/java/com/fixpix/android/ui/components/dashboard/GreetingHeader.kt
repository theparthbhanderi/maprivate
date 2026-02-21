package com.fixpix.android.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * GreetingHeader Component - Matches audit spec
 * 
 * Spec from Audit:
 * - Greeting: 24sp Bold, -0.6sp letter-spacing
 * - Subtitle: 17sp Regular, Secondary color
 */
@Composable
fun GreetingHeader(
    username: String,
    subtitle: String = "Ready to restore some memories?",
    modifier: Modifier = Modifier
) {
    val timeOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val greeting = when {
        timeOfDay < 12 -> "Good Morning"
        timeOfDay < 17 -> "Good Afternoon"
        else -> "Good Evening"
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Greeting H1 - 24sp Bold, -0.6sp letter-spacing
        Text(
            text = "$greeting, $username! 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = FixPixTheme.colors.textMain,
            letterSpacing = (-0.6).sp
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Subtitle - 17sp Regular
        Text(
            text = subtitle,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = FixPixTheme.colors.textSecondary,
            letterSpacing = (-0.4).sp
        )
    }
}
