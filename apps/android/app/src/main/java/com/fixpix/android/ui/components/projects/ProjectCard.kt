package com.fixpix.android.ui.components.projects

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fixpix.android.ui.theme.FixPixMotion
import com.fixpix.android.ui.theme.FixPixTheme

/**
 * Project Status
 */
enum class ProjectStatus {
    Processing,
    Completed,
    Failed
}

/**
 * Project UI Model
 */
data class ProjectUiModel(
    val id: String,
    val title: String,
    val thumbnailUrl: String?,
    val status: ProjectStatus,
    val createdAt: String,
    val processingProgress: Float = 0f // 0-1
)

/**
 * ProjectCard - Gallery card matching website design
 * 
 * Features:
 * - Rounded corners (12dp)
 * - Thumbnail with aspect ratio
 * - Status overlay
 * - Title + meta info
 * - Spring press animation
 */
@Composable
fun ProjectCard(
    project: ProjectUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Spring scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) FixPixMotion.SCALE_PRESSED else FixPixMotion.SCALE_NORMAL,
        animationSpec = FixPixMotion.springPress,
        label = "cardScale"
    )
    
    Column(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(FixPixTheme.colors.surface)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            )
    ) {
        // Thumbnail with status overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .background(FixPixTheme.colors.fill.copy(alpha = 0.1f))
        ) {
            // Thumbnail image
            if (project.thumbnailUrl != null) {
                AsyncImage(
                    model = project.thumbnailUrl,
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Status overlay
            when (project.status) {
                ProjectStatus.Processing -> {
                    // Dark overlay with progress
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                progress = { project.processingProgress },
                                color = FixPixTheme.colors.primary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Processing...",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                ProjectStatus.Failed -> {
                    // Error overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(FixPixTheme.colors.error.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Failed",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                ProjectStatus.Completed -> {
                    // Checkmark badge (top-right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .background(
                                FixPixTheme.colors.success,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            // Bottom gradient for text readability
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
            )
        }
        
        // Title & meta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = project.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = FixPixTheme.colors.textMain,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.createdAt,
                fontSize = 12.sp,
                color = FixPixTheme.colors.textSecondary
            )
        }
    }
}
