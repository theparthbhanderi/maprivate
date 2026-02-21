package com.fixpix.ui.editor.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.ButtonSize
import com.fixpix.ui.components.ButtonVariant
import com.fixpix.ui.components.FixPixButton
import com.fixpix.ui.components.FixPixGlassSurface
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun EditorActionBar(
    onGenerateClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    isProcessing: Boolean,
    modifier: Modifier = Modifier
) {
    FixPixGlassSurface(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cancel / Upload New
            FixPixButton(
                onClick = onCancelClick,
                variant = ButtonVariant.Secondary,
                size = ButtonSize.Md,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            // Generate / Update (Primary)
            FixPixButton(
                onClick = onGenerateClick,
                variant = ButtonVariant.Primary,
                size = ButtonSize.Md,
                modifier = Modifier.weight(2f), // Larger
                loading = isProcessing
            ) {
                Text("Generate")
            }
        }
    }
}
