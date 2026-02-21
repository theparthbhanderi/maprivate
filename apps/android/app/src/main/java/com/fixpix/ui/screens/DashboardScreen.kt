package com.fixpix.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixCard
import com.fixpix.ui.components.FixPixScaffold
import com.fixpix.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onNewProjectClick: () -> Unit,
    onProjectClick: (String) -> Unit,
    onGalleryClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    FixPixScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Generic Fade In for Header
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                Column {
                    Text(
                        text = "Good Morning,",
                        style = FixPixTheme.typography.subhead,
                        color = FixPixTheme.colors.textSecondary
                    )
                    Text(
                        text = "Parth",
                        style = FixPixTheme.typography.largeTitle,
                        color = FixPixTheme.colors.textPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stats Row
            // In a real app we might animate these sequentially, handled by a Staggered list or similar
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 100)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                         label = "Restored",
                         value = "12",
                         color = FixPixTheme.colors.primary,
                         modifier = Modifier.weight(1f)
                    )
                    StatCard(
                         label = "Time Saved",
                         value = "2h",
                         color = FixPixTheme.colors.success,
                         modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Quick Actions
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                 Column {
                    Text(
                        text = "Quick Actions",
                        style = FixPixTheme.typography.headline,
                        color = FixPixTheme.colors.textPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    val actions = listOf(
                        Triple("New Project", Icons.Default.Add, onNewProjectClick),
                        Triple("Batch Process", Icons.Default.Refresh, {}),
                        Triple("All Projects", Icons.Default.Star, onGalleryClick),
                        Triple("History", Icons.Default.History, {})
                    )
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(actions.size) { index -> 
                            val (label, icon, onClick) = actions[index]
                            QuickActionCard(label = label, icon = icon, onClick = onClick)
                        }
                    }
                }
            }
            
            // CTA Banner (Gradient)
            AnimatedVisibility(visible = visible, enter = fadeIn(tween(600, delayMillis = 400))) {
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .height(100.dp)
                         .background(
                             brush = Brush.horizontalGradient(
                                 colors = listOf(FixPixTheme.colors.primary, FixPixTheme.colors.tertiary)
                             ),
                             shape = FixPixTheme.shapes.xl
                         )
                         .padding(20.dp),
                     contentAlignment = Alignment.CenterStart
                 ) {
                     Text(
                         text = "Start Restoring Now",
                         style = FixPixTheme.typography.title3,
                         color = androidx.compose.ui.graphics.Color.White
                     )
                 }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    FixPixCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = value,
                style = FixPixTheme.typography.title1,
                color = color
            )
            Text(
                text = label,
                style = FixPixTheme.typography.caption1,
                color = FixPixTheme.colors.textSecondary
            )
        }
    }
}

@Composable
fun QuickActionCard(label: String, icon: ImageVector, onClick: () -> Unit) {
    FixPixCard(
        onClick = onClick,
        modifier = Modifier.height(120.dp) // Square-ish
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FixPixTheme.colors.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = label,
                style = FixPixTheme.typography.headline,
                color = FixPixTheme.colors.textPrimary
            )
        }
    }
}
