package com.fixpix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.FixPixCard
import com.fixpix.ui.components.FixPixScaffold
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun GalleryScreen(
    onProjectClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    FixPixScaffold(
        topBarTitle = "My Projects",
        onBackClick = onBackClick
    ) { padding ->
        // Fake Data
        val projects = List(10) { "Project ${it + 1}" }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp, 
                bottom = 16.dp, 
                start = 16.dp, 
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(projects.size) { index ->
                ProjectThumbnailCard(
                    title = projects[index],
                    status = if(index % 3 == 0) "Processing" else "Completed",
                    onClick = { onProjectClick(projects[index]) }
                )
            }
        }
    }
}

@Composable
fun ProjectThumbnailCard(
    title: String,
    status: String,
    onClick: () -> Unit
) {
    FixPixCard(
        onClick = onClick,
        modifier = Modifier.aspectRatio(0.8f), // Tall portrait card
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Placeholder Image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FixPixTheme.colors.background)
                ) {
                    Text(
                        text = "IMG",
                        modifier = Modifier.align(Alignment.Center),
                        color = FixPixTheme.colors.textTertiary
                    )
                }

                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 300f
                            )
                        )
                )

                // Title & Status
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = title,
                        style = FixPixTheme.typography.headline,
                        color = Color.White
                    )
                    Text(
                        text = status,
                        style = FixPixTheme.typography.caption1,
                        color = if (status == "Processing") FixPixTheme.colors.warning else FixPixTheme.colors.success
                    )
                }
            }
        }
    )
}
