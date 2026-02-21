package com.fixpix.android.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * StatCard Component - Matches audit spec exactly
 * 
 * Spec from Audit:
 * - Padding: 24dp all sides
 * - Spacing: 8dp between elements
 * - Corner Radius: 16dp
 * - Background: White
 * - Shadow: rgba(0,0,0,0.06) 0px 4px 16px
 * - Icon: 32x32dp
 * - Value: 32sp Bold
 * - Label: 15sp Regular, Secondary color
 */
@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    iconTint: Color = FixPixTheme.colors.textSecondary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = FixPixTheme.shapes.large,
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(FixPixTheme.shapes.large)
            .background(FixPixTheme.colors.surface)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon - 32x32dp
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Value - 32sp Bold
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = FixPixTheme.colors.textMain
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Label - 15sp Regular
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = FixPixTheme.colors.textSecondary
            )
        }
    }
}
