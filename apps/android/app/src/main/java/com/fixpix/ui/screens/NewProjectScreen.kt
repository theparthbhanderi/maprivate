package com.fixpix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixButton
import com.fixpix.ui.components.FixPixScaffold
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun NewProjectScreen(
    onUploadClick: () -> Unit
) {
    FixPixScaffold(
        topBarTitle = "New Project",
        onBackClick = {} // Dummy back action
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Upload Zone
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(FixPixTheme.shapes.lg)
                    .background(FixPixTheme.colors.background) // Slightly distinct?
                    .border(
                        width = 2.dp,
                        color = FixPixTheme.colors.separator,
                        shape = FixPixTheme.shapes.lg
                    )
                    .clickable { onUploadClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload",
                        modifier = Modifier.size(48.dp),
                        tint = FixPixTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tap to upload image",
                        style = FixPixTheme.typography.headline,
                        color = FixPixTheme.colors.textSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Supports JPG, PNG, WEBP up to 25MB",
                style = FixPixTheme.typography.caption1,
                color = FixPixTheme.colors.textTertiary
            )

            Spacer(modifier = Modifier.height(24.dp))

            FixPixButton(
                onClick = onUploadClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select from Gallery")
            }
        }
    }
}
