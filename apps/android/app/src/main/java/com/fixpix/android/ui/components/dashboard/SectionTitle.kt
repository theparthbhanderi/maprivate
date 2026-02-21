package com.fixpix.android.ui.components.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * SectionTitle Component - Matches audit spec
 * 
 * Spec from Audit:
 * - Section Title: 20sp SemiBold, -0.4sp letter-spacing
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = FixPixTheme.colors.textMain,
        letterSpacing = (-0.4).sp,
        modifier = modifier.padding(vertical = 8.dp)
    )
}
