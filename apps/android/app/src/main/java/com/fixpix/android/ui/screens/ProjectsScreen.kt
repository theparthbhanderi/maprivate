package com.fixpix.android.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.components.projects.ProjectCard
import com.fixpix.android.ui.components.projects.ProjectStatus
import com.fixpix.android.ui.components.projects.ProjectUiModel
import com.fixpix.android.ui.components.projects.ProjectsEmptyState
import com.fixpix.android.ui.components.projects.ProjectsLoadingGrid
import com.fixpix.android.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

/**
 * Gallery/Projects State
 */
sealed class ProjectsState {
    data object Loading : ProjectsState()
    data object Empty : ProjectsState()
    data class Success(val projects: List<ProjectUiModel>) : ProjectsState()
    data class Error(val message: String) : ProjectsState()
}

/**
 * ProjectsScreen - Gallery/Projects list matching website
 * 
 * Layout:
 * - Header with title
 * - LazyVerticalGrid (2 columns on mobile)
 * - 12dp gaps
 * - Empty state
 * - Loading skeleton grid
 */
@Composable
fun ProjectsScreen(
    navController: NavController
) {
    // Mock data for now - will be replaced with ViewModel
    var state by remember { mutableStateOf<ProjectsState>(ProjectsState.Loading) }
    var showContent by remember { mutableStateOf(false) }
    
    // Simulate loading
    LaunchedEffect(Unit) {
        delay(1000)
        // Mock projects - replace with real data
        val mockProjects = listOf(
            ProjectUiModel(
                id = "1",
                title = "Family Photo 1957",
                thumbnailUrl = null,
                status = ProjectStatus.Completed,
                createdAt = "2 hours ago"
            ),
            ProjectUiModel(
                id = "2",
                title = "Wedding Portrait",
                thumbnailUrl = null,
                status = ProjectStatus.Processing,
                createdAt = "1 day ago",
                processingProgress = 0.6f
            ),
            ProjectUiModel(
                id = "3",
                title = "Childhood Memory",
                thumbnailUrl = null,
                status = ProjectStatus.Completed,
                createdAt = "3 days ago"
            ),
            ProjectUiModel(
                id = "4",
                title = "Grandparents",
                thumbnailUrl = null,
                status = ProjectStatus.Failed,
                createdAt = "1 week ago"
            )
        )
        state = ProjectsState.Success(mockProjects)
        delay(100)
        showContent = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FixPixTheme.colors.background)
    ) {
        when (val currentState = state) {
            is ProjectsState.Loading -> {
                ProjectsLoadingGrid()
            }
            is ProjectsState.Empty -> {
                ProjectsEmptyState(
                    onStartClick = {
                        navController.navigate(Screen.Editor.createRoute("new"))
                    }
                )
            }
            is ProjectsState.Success -> {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(300)) + 
                            slideInVertically(
                                animationSpec = tween(300),
                                initialOffsetY = { it / 4 }
                            )
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
                        Column(
                            modifier = Modifier.padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 48.dp,
                                bottom = 12.dp
                            )
                        ) {
                            Text(
                                text = "My Projects",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = FixPixTheme.colors.textMain,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${currentState.projects.size} restorations",
                                fontSize = 15.sp,
                                color = FixPixTheme.colors.textSecondary
                            )
                        }
                        
                        // Grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 100.dp // For bottom nav
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = currentState.projects,
                                key = { it.id }
                            ) { project ->
                                ProjectCard(
                                    project = project,
                                    onClick = {
                                        navController.navigate(Screen.Editor.createRoute(project.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
            is ProjectsState.Error -> {
                // Error state - similar to empty but with error message
                ProjectsEmptyState(
                    onStartClick = {
                        state = ProjectsState.Loading
                    }
                )
            }
        }
    }
}
