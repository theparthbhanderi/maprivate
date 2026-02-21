package com.fixpix.android.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.components.dashboard.GreetingHeader
import com.fixpix.android.ui.components.dashboard.QuickActionCard
import com.fixpix.android.ui.components.dashboard.SectionTitle
import com.fixpix.android.ui.components.dashboard.ShimmerStatCard
import com.fixpix.android.ui.components.dashboard.StatCard
import com.fixpix.android.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

/**
 * DashboardScreen - Exact clone of FixPix website mobile dashboard
 * 
 * Layout from Audit:
 * - Edge-to-edge with radial blue gradient background
 * - LazyColumn with contentPadding = 20dp
 * - Major section spacing = 24dp
 * - Greeting header
 * - 3 vertical stat cards
 * - Quick Actions section with 2 cards
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val projects by viewModel.projects.collectAsState()
    val loading by viewModel.loading.collectAsState()
    
    // Screen entry animation state
    var showContent by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }
    
    // Mock stats (will be replaced with real data from ViewModel)
    val imagesRestored = projects.size.coerceAtLeast(24)
    val timeSaved = "3h"
    val aiEnhancements = 156
    
    // Radial blue gradient background - matches audit
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FixPixTheme.colors.background)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        FixPixTheme.colors.primary.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = 800f
                )
            )
            // Second gradient in bottom-right
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        FixPixTheme.colors.primary.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(Float.MAX_VALUE, Float.MAX_VALUE),
                    radius = 600f
                )
            )
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(300)) + 
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it / 4 }
                    )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Safe area spacer for status bar
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Greeting Header
                item {
                    GreetingHeader(
                        username = "androidtest",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                // Stats Section - 24dp spacing between section and cards
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Stat Card 1: Images Restored
                item {
                    if (loading) {
                        ShimmerStatCard()
                    } else {
                        StatCard(
                            icon = Icons.Outlined.Image,
                            value = imagesRestored.toString(),
                            label = "Images Restored"
                        )
                    }
                }
                
                // Stat Card 2: Time Saved
                item {
                    if (loading) {
                        ShimmerStatCard()
                    } else {
                        StatCard(
                            icon = Icons.Outlined.Schedule,
                            value = timeSaved,
                            label = "Time Saved"
                        )
                    }
                }
                
                // Stat Card 3: AI Enhancements
                item {
                    if (loading) {
                        ShimmerStatCard()
                    } else {
                        StatCard(
                            icon = Icons.Outlined.AutoFixHigh,
                            value = aiEnhancements.toString(),
                            label = "AI Enhancements"
                        )
                    }
                }
                
                // Quick Actions Section - 24dp top spacing
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionTitle(title = "Quick Actions")
                }
                
                // Quick Action Cards Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        QuickActionCard(
                            icon = Icons.Outlined.Restore,
                            label = "Restoration",
                            onClick = { 
                                navController.navigate(Screen.Editor.createRoute("new"))
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        QuickActionCard(
                            icon = Icons.Outlined.PhotoLibrary,
                            label = "Projects",
                            onClick = { 
                                navController.navigate(Screen.Projects.route)
                            },
                            iconTint = FixPixTheme.colors.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Bottom padding for navigation bar
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
